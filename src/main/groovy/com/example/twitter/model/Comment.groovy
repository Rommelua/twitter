package com.example.twitter.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

import java.time.LocalDateTime

@Document
class Comment {
    @Id
    String id
    String content
    LocalDateTime date
    @DocumentReference
    User user
}
