package com.ibrahimahmads.github.vbooth.dto.request;

import com.ibrahimahmads.github.vbooth.utils.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderUserSearch {
    private String keyword;
    private OrderStatus status;
}
