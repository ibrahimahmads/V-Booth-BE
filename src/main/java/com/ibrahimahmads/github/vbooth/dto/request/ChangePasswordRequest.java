package com.ibrahimahmads.github.vbooth.dto.request;

import java.util.UUID;

public record ChangePasswordRequest(
        UUID id,
        String oldPassword,
        String newPassword
) {
}
