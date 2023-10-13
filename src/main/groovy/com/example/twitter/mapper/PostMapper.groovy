package com.example.twitter.mapper

import com.example.twitter.dto.post.AddPostRequestDto
import com.example.twitter.dto.post.PostDto
import com.example.twitter.model.Post

interface PostMapper {

    PostDto toDto(Post post)

    Post toModel(AddPostRequestDto dto)

}