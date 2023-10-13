package com.example.twitter.controller

import com.example.twitter.config.ContainerBaseSpec
import com.example.twitter.dto.post.PostDto
import com.example.twitter.model.Comment
import com.example.twitter.model.Like
import com.example.twitter.model.Post
import com.example.twitter.model.User
import com.example.twitter.repository.PostRepository
import com.example.twitter.repository.UserRepository
import com.example.twitter.security.JwtUtil
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.testcontainers.spock.Testcontainers

import java.time.LocalDateTime

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class PostControllerSpec extends ContainerBaseSpec {
    @Autowired
    PostRepository postRepository
    @Autowired
    UserRepository userRepository
    @Autowired
    PasswordEncoder passwordEncoder
    @Autowired
    JwtUtil jwtUtil

    @Autowired
    ObjectMapper objectMapper

    @Autowired
    MockMvc mockMvc

    String token
    User user


    def setup() {
        String encodedPassword = passwordEncoder.encode("password")
        user = new User(email: "user", password: encodedPassword)
        token = jwtUtil.generateToken("user")
        userRepository.save(user)
    }

    def cleanup() {
        userRepository.deleteAll()
        postRepository.deleteAll()
    }


    def "get | OK"() {
        given:
        String postId = "123"
        Post post = new Post(id: postId, content: "content", user: user)
        postRepository.save(post)

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .get("/posts/" + postId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()

        then:
        PostDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), PostDto.class)
        actual.id == postId
        actual.user.email == user.email
        actual.content == "content"
    }

    def "getPostsFeed | default sorting by date desc | OK"() {
        given:
        addUsersAndPosts()

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders
                .get("/posts")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()

        then:
        PostDto[] actual = objectMapper.readValue(result.getResponse().getContentAsString(), PostDto[].class)
        actual.length == 3
        PostDto post_2_2 = actual[0]
        post_2_2.content == "content_2_2"
        post_2_2.user.email == "user_2"

        PostDto post_1 = actual[1]
        post_1.content == "content_1"
        post_1.user.email == "user_1"
        post_1.likes.size() == 2
        post_1.likes.get(0).user.email == "user_1"
        post_1.likes.get(1).user.email == "user_2"
        post_1.comments.size() == 1
        post_1.comments.get(0).user.email == "user_3"
        post_1.comments.get(0).content == "comment"

        PostDto post_2_1 = actual[2]
        post_2_1.content == "content_2_1"
        post_2_1.user.email == "user_2"

    }

    private void addUsersAndPosts() {
        User user_1 = new User(email: "user_1")
        User user_2 = new User(email: "user_2")
        User user_3 = new User(email: "user_3")
        userRepository.saveAll([user_1, user_2, user_3])
        user.subscriptions = [user_1, user_2]
        userRepository.save(user)
        LocalDateTime now = LocalDateTime.now()
        Post post_1 = new Post(content: "content_1", user: user_1, date: now)
        Comment comment = new Comment(user: user_3, content: "comment")
        Like like_1 = new Like(user: user_1)
        Like like_2 = new Like(user: user_2)
        post_1.comments = [comment]
        post_1.likes = [like_1, like_2]

        Post post_2_1 = new Post(content: "content_2_1", user: user_2, date: now.minusHours(1))
        Post post_2_2 = new Post(content: "content_2_2", user: user_2, date: now.plusHours(1))
        Post post_3 = new Post(content: "content_3", user: user_3)

        postRepository.saveAll([post_1, post_2_1, post_2_2, post_3])
    }
}
