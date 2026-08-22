package com.ibrahimahmads.github.vbooth.dto.request;

import java.util.UUID;

public record SetInitialPasswordRequest(
        UUID id,
        String password
) {
}
