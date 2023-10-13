package com.example.twitter.controller

import com.example.twitter.dto.post.AddCommentRequestDto
import com.example.twitter.dto.post.AddPostRequestDto
import com.example.twitter.dto.post.PostDto
import com.example.twitter.model.User
import com.example.twitter.service.PostService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

import javax.validation.Valid

@Tag(name = "Post management", description = "Endpoints for managing posts")
@RestController()
@RequestMapping("/posts")
class PostController {
    private final PostService postService

    PostController(PostService postService) {
        this.postService = postService
    }

    @GetMapping("/{id}")
    PostDto get(@PathVariable String id) {
        return postService.get(id)
    }

    @GetMapping()
    List<PostDto> getPostsFeed(Authentication authentication,
                               @RequestParam(defaultValue = "0") int pageNo,
                               @RequestParam(defaultValue = "10") int pageSize,
                               @RequestParam(defaultValue = "date") String sortBy) {
        User user = (User) authentication.getPrincipal()
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending())
        return postService.getPostsFeed(user.id, pageable)
    }

    @GetMapping("/feed/{userId}")
    List<PostDto> getPostsFeed(@PathVariable String userId, Pageable pageable) {
        return postService.getPostsFeed(userId, pageable)
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    PostDto add(@RequestBody @Valid AddPostRequestDto request, Authentication authentication) {
        User user = (User) authentication.getPrincipal()
        return postService.add(user.id, request)
    }

    @PutMapping("/{id}")
    PostDto update(@PathVariable String id, @RequestBody @Valid AddPostRequestDto request,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal()
        return postService.update(user.id, id, request)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable String id, Authentication authentication) {
        User user = (User) authentication.getPrincipal()
        postService.delete(user.id, id)
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/{id}/likes")
    PostDto addLike(@PathVariable String id, Authentication authentication) {
        User user = (User) authentication.getPrincipal()
        return postService.addLike(user.id, id)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}/likes")
    void deleteLike(@PathVariable String id, Authentication authentication) {
        User user = (User) authentication.getPrincipal()
        postService.deleteLike(user.getId(), id)
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/{id}/comments")
    PostDto addComment(@PathVariable String id, Authentication authentication,
                       @RequestBody @Valid AddCommentRequestDto request) {
        User user = (User) authentication.getPrincipal()
        return postService.addComment(user.id, id, request)
    }
}
