package com.ibrahimahmads.github.vbooth.dto.request;

import com.ibrahimahmads.github.vbooth.utils.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventUserSeach {
    private String keyword;
    private EventStatus status;
}
