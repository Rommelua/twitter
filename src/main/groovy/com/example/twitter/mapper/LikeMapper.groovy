package com.example.twitter.mapper

import com.example.twitter.dto.post.LikeDto
import com.example.twitter.model.Like

interface LikeMapper {

    LikeDto toDto(Like like)
}