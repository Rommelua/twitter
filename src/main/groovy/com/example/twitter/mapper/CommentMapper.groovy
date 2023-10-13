package com.example.twitter.mapper

import com.example.twitter.dto.post.CommentDto
import com.example.twitter.model.Comment

interface CommentMapper {

    CommentDto toDto(Comment comment)
}