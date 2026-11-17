package com.ibrahimahmads.github.vbooth.controller;

import com.ibrahimahmads.github.vbooth.dto.response.CheckoutResponse;
import com.ibrahimahmads.github.vbooth.dto.response.OrderResponse;
import com.ibrahimahmads.github.vbooth.entity.Orders;
import com.ibrahimahmads.github.vbooth.security.model.UserContext;
import com.ibrahimahmads.github.vbooth.service.OrderService;
import com.ibrahimahmads.github.vbooth.service.OrderStreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderStreamService orderStreamService;

    @PostMapping("/checkout/qris")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CheckoutResponse> checkoutWithQris(@AuthenticationPrincipal UserContext userContext, @RequestParam UUID paket){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrderByQris(userContext.getAccountId(),paket));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrder(){
        return ResponseEntity.ok().body(orderService.getAllOrders());
    }

    @GetMapping("/me/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrderResponse>> getAllMyOrder(@AuthenticationPrincipal UserContext userContext){
        return ResponseEntity.ok().body(orderService.getAllOrdersByAccountId(userContext.getAccountId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Orders> getOrderById(@PathVariable UUID id){
        return ResponseEntity.ok().body(orderService.getOrderById(id));
    }

    @GetMapping("/me/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Orders> getMyOrderById(@PathVariable UUID id, @AuthenticationPrincipal UserContext userContext){
        return ResponseEntity.ok().body(orderService.getOrderByIdAndAccountId(id,userContext.getAccountId()));
    }

    @GetMapping(value = "/me/{orderNumber}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("hasRole('USER')")
    public SseEmitter streamMyOrder(@PathVariable String orderNumber,
                                    @AuthenticationPrincipal UserContext userContext) {
        return orderStreamService.subscribe(orderNumber, userContext.getAccountId());
    }
}
