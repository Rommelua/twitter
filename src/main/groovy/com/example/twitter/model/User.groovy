package com.example.twitter.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document
class User implements UserDetails {
    @Id
    String id
    String email
    String password
    String firstName
    String lastName
    @DocumentReference(lazy=true)
    List<Post> posts
    Set<User> subscriptions = new HashSet<>()

    @Override
    boolean equals(Object o) {
        if (this.is(o)) {
            return true
        }
        if (getClass() != o.class) {
            return false
        }

        User user = (User) o
        return email == user.email
    }

    @Override
    int hashCode() {
        return email.hashCode()
    }

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList()
    }

    @Override
    String getUsername() {
        return email
    }

    @Override
    boolean isAccountNonExpired() {
        return true
    }

    @Override
    boolean isAccountNonLocked() {
        return true
    }

    @Override
    boolean isCredentialsNonExpired() {
        return true
    }

    @Override
    boolean isEnabled() {
        return true
    }
}
