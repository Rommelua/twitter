package com.example.twitter.dto.user

import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class UserRegistrationRequestDto {
    @Email
    @NotNull
    String email
    @NotNull
    @Size(min = 4)
    String password
    @NotNull
    String firstName
    @NotNull
    String lastName
}
