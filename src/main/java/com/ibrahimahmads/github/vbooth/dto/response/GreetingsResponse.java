package com.ibrahimahmads.github.vbooth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GreetingsResponse {
    private String guestName;
    private String photoUrl;
    private String audioUrl;
    private String tgl;
    private String jam;
}
