package org.mfnm.musicapi.domain.user;

public record UserRequestDTO(String usernameOrEmail, String password) {
}
