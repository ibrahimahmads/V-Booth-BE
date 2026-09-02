package com.ibrahimahmads.github.vbooth.dto.request;

public record ChangePasswordRequest(
        String oldPassword,
        String newPassword
) {
}
