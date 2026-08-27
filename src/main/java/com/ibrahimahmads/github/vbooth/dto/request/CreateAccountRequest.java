package com.ibrahimahmads.github.vbooth.dto.request;

public record CreateAccountRequest(
        String name,
        String username,
        String email,
        String password
) {
}
