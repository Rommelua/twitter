package com.example.twitter.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference

import java.time.LocalDateTime

@Document
class Post {
    @Id
    String id
    @DocumentReference
    User user
    String content
    LocalDateTime date
    List<Comment> comments = new ArrayList<>()
    Set<Like> likes = new HashSet<>()
}
