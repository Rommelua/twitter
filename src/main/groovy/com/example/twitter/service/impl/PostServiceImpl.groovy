package com.example.twitter.service.impl

import com.example.twitter.dto.post.AddCommentRequestDto
import com.example.twitter.dto.post.AddPostRequestDto
import com.example.twitter.dto.post.PostDto
import com.example.twitter.exception.EntityNotFoundException
import com.example.twitter.mapper.PostMapper
import com.example.twitter.model.Comment
import com.example.twitter.model.Like
import com.example.twitter.model.Post
import com.example.twitter.model.User
import com.example.twitter.repository.PostRepository
import com.example.twitter.repository.UserRepository
import com.example.twitter.service.PostService
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.time.LocalDateTime
import java.util.stream.Collectors

@Service
class PostServiceImpl implements PostService {
    private final PostRepository postRepository
    private final UserRepository userRepository
    private final PostMapper postMapper
    private final MongoTemplate mongoTemplate

    PostServiceImpl(PostRepository postRepository, PostMapper postMapper,
                    UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.postRepository = postRepository
        this.postMapper = postMapper
        this.userRepository = userRepository
        this.mongoTemplate = mongoTemplate
    }

    @Override
    PostDto get(String id) {
        Post post = getPostById(id)
        return postMapper.toDto(post)
    }

    @Override
    @Transactional
    PostDto add(String userId, AddPostRequestDto dto) {
        User user = getUserById(userId)
        Post post = postMapper.toModel(dto)
        post.user = user
        post = postRepository.save(post)
        user.posts.add(post)
        userRepository.save(user)
        return postMapper.toDto(post)
    }

    @Override
    @Transactional
    PostDto update(String userId, String id, AddPostRequestDto dto) {
        Post post = getPostById(id)
        if (userId != post.user.id) {
            throw new UnsupportedOperationException("Post with id " + id + " is not belongs to user with id " + userId)
        }
        post.content = dto.content
        postRepository.save(post)
        return postMapper.toDto(post)
    }

    @Override
    @Transactional
    void delete(String userId, String id) {
        Post post = getPostById(id)
        if (userId != post.user.id) {
            throw new UnsupportedOperationException("Post with id " + id + " is not belongs to user with id " + userId)
        }
        postRepository.deleteById(id)
    }

    @Override
    @Transactional
    PostDto addLike(String userId, String id) {
        Post post = getPostById(id)
        User user = getUserById(userId)
        Like like = new Like(user: user)
        post.likes.add(like)
        postRepository.save(post)
        return postMapper.toDto(post)
    }

    @Override
    @Transactional
    PostDto deleteLike(String userId, String id) {
        Post post = getPostById(id)
        User user = getUserById(userId)
        Like like = new Like(user: user)
        post.likes.remove(like)
        postRepository.save(post)
        return postMapper.toDto(post)
    }

    @Override
    @Transactional
    PostDto addComment(String userId, String id, AddCommentRequestDto dto) {
        Post post = getPostById(id)
        User user = getUserById(userId)
        Comment comment = new Comment(content: dto.content, date: LocalDateTime.now(), user: user)
        post.comments.add(comment)
        postRepository.save(post)
        return postMapper.toDto(post)
    }

    @Override
    List<PostDto> getPostsFeed(String userId, Pageable pageable) {
        User user = getUserById(userId)
        List<Post> posts = postRepository.findByUserIn(user.subscriptions, pageable)
        return posts.stream()
                 .map(p -> postMapper.toDto(p))
                 .collect(Collectors.toList())
    }

    private Post getPostById(String id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't found post with id: " + id))
    }

    private User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Can't found user with id: " + userId))
    }
}
