package com.ibrahimahmads.github.vbooth.dto.response.midtrans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActionResponse {
    private String name;
    private String method;
    private String url;
}
