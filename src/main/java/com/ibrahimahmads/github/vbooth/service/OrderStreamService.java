package com.ibrahimahmads.github.vbooth.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

public interface OrderStreamService {
    SseEmitter subscribe(String orderId, UUID accountId);
}
