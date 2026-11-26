package com.ibrahimahmads.github.vbooth.dto.response;

import com.ibrahimahmads.github.vbooth.utils.enums.EventStatus;
import com.ibrahimahmads.github.vbooth.utils.enums.EventTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EventResponse {
    private UUID id;
    private String eventName;
    private String slug;
    private String eventDescription;
    private String eventDate;
    private String eventStartTime;
    private String eventEndTime;
    private String eventLocation;
    private Integer numberOfGreetings;
    private EventStatus eventStatus;
    private EventTypes eventType;
    private PaketInfo paket;

    @Getter
    @Builder
    public static class PaketInfo {
        private UUID id;
        private String name;
        private Integer maxGreetings;
    }
}
