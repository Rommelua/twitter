package com.example.twitter.repository

import com.example.twitter.model.Post
import com.example.twitter.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUserIn(Collection<User> users, Pageable pageable)
}