package com.example.twitter.model


import org.springframework.data.mongodb.core.mapping.Document

@Document
class Like {
    User user

    @Override
    boolean equals(Object o) {
        if (this.is(o)) {
            return true
        }
        if (getClass() != o.class) {
            return false
        }

        Like like = (Like) o
        return user == like.user
    }

    @Override
    int hashCode() {
        return user.hashCode()
    }
}
