package com.example.twitter.repository

import com.example.twitter.model.User
import org.springframework.data.mongodb.repository.ExistsQuery
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository extends MongoRepository<User, String> {
    @ExistsQuery("{ 'email': ?0}")
    boolean existsByEmail(String email)

    User findByEmail(String email)
}