package com.example

import io.ktor.http.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.delete
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun Application.configureResources() {
    install(Resources)
    routing {

        get<Posts> {
            call.respond(
                newSuspendedTransaction(Dispatchers.IO) {
                    Post.selectAll().map {
                        PostData(it[Post.id].value, it[Post.text], it[Post.timestamp])
                    }
                }
            )
        }

        get<Posts.Since> { params ->
            val sinceTimestamp = params.timestamp
            call.respond(
                newSuspendedTransaction(Dispatchers.IO) {
                    Post.selectAll().where { Post.timestamp greater sinceTimestamp }
                        .map { PostData(it[Post.id].value, it[Post.text], it[Post.timestamp]) }
                }
            )
        }

        get<Posts.ById> { params ->
            val postId = params.id
            val post = newSuspendedTransaction(Dispatchers.IO) {
                Post.selectAll().where { Post.id eq postId }
                    .singleOrNull()?.let {
                        PostData(it[Post.id].value, it[Post.text], it[Post.timestamp])
                    }
            }
            if (post != null) {
                call.respond(post)
            } else {
                call.respondText("Post not found", status = HttpStatusCode.NotFound)
            }
        }

        post<Posts> {
            val postInput = call.receive<PostCreateData>()
            val postId = newSuspendedTransaction(Dispatchers.IO) {
                Post.insert {
                    it[text] = postInput.text
                    it[timestamp] = System.currentTimeMillis()
                } get Post.id
            }
            call.respondText("Post created with ID: $postId")
        }

        delete<Posts.ById> { params ->
            val postId = params.id
            newSuspendedTransaction(Dispatchers.IO) {
                Post.deleteWhere { Post.id eq postId }
            }
            call.respondText("Post deleted")
        }
    }
}

@Serializable data class PostCreateData(val text: String)
@Serializable data class PostData(val id: Int, val text: String, val timestamp: Long)

@Resource("/posts")
class Posts {
    @Resource("{id}")
    class ById(val parent: Posts = Posts(), val id: Int)

    @Resource("since/{timestamp}")
    class Since(val parent: Posts = Posts(), val timestamp: Long)
}