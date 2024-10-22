package com.example

import org.jetbrains.exposed.dao.id.IntIdTable

object Post : IntIdTable() {
    val text = varchar("text", 500)
    val timestamp = long("timestamp")
}