package com.ibrahimahmads.github.vbooth.dto.request;

public record PaketRequest(
        String name,
        String description,
        Integer eventActivePeriod,
        Integer galeryActivePeriod,
        Integer maxGuest,
        Integer price
) {
}
