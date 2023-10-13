package com.example.twitter.service

import com.example.twitter.dto.post.AddCommentRequestDto
import com.example.twitter.dto.post.AddPostRequestDto
import com.example.twitter.dto.post.PostDto

import org.springframework.data.domain.Pageable

interface PostService {
    PostDto get(String id)

    PostDto add(String userId, AddPostRequestDto dto)

    PostDto update(String userId, String id, AddPostRequestDto dto)

    void delete(String userId, String id)

    PostDto addLike(String userId, String id)

    PostDto deleteLike(String userId, String id)

    PostDto addComment(String userId, String id, AddCommentRequestDto dto)

    List<PostDto> getPostsFeed(String userId, Pageable pageable)
}