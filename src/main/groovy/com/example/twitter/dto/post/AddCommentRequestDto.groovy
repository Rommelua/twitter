package com.example.twitter.dto.post

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class AddCommentRequestDto {
    @NotNull
    @Size(min = 5, max = 280)
    String content
}
