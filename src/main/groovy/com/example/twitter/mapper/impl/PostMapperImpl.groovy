package com.example.twitter.mapper.impl

import com.example.twitter.dto.post.AddPostRequestDto
import com.example.twitter.dto.post.CommentDto
import com.example.twitter.dto.post.LikeDto
import com.example.twitter.dto.post.PostDto
import com.example.twitter.dto.user.UserResponseDtoWithoutSubscriptions
import com.example.twitter.mapper.CommentMapper
import com.example.twitter.mapper.LikeMapper
import com.example.twitter.mapper.PostMapper
import com.example.twitter.mapper.UserMapper
import com.example.twitter.model.Post
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

import java.time.LocalDateTime
import java.util.stream.Collectors

@Component
class PostMapperImpl implements PostMapper {
    private final UserMapper userMapper
    private final CommentMapper commentMapper
    private final LikeMapper likeMapper

    PostMapperImpl(UserMapper userMapper, CommentMapper commentMapper, LikeMapper likeMapper) {
        this.userMapper = userMapper
        this.commentMapper = commentMapper
        this.likeMapper = likeMapper
    }

    @Override
    PostDto toDto(Post post) {
        UserResponseDtoWithoutSubscriptions user = userMapper.toDtoWithoutSubscriptions(post.user)
        List<CommentDto> comments = post.comments.stream()
                .map(c -> commentMapper.toDto(c))
                .collect(Collectors.toList())
        List<LikeDto> likes = post.likes.stream()
                .map(l -> likeMapper.toDto(l))
                .collect(Collectors.toList())
        return new PostDto(id: post.id, user: user, content: post.content, date: post.date,
                comments: comments, likes: likes)
    }

    @Override
    Post toModel(AddPostRequestDto dto) {
        return new Post(content: dto.content, date: LocalDateTime.now())
    }
}
