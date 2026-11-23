package com.ibrahimahmads.github.vbooth.controller;

import com.ibrahimahmads.github.vbooth.dto.request.OrderUserSearch;
import com.ibrahimahmads.github.vbooth.dto.response.CheckoutResponse;
import com.ibrahimahmads.github.vbooth.dto.response.OrderResponse;
import com.ibrahimahmads.github.vbooth.dto.response.PageResponseWrapper;
import com.ibrahimahmads.github.vbooth.entity.Orders;
import com.ibrahimahmads.github.vbooth.security.model.UserContext;
import com.ibrahimahmads.github.vbooth.service.OrderService;
import com.ibrahimahmads.github.vbooth.service.OrderStreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
    public ResponseEntity<PageResponseWrapper<OrderResponse>> getAllOrder(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt")
            String sortBy,
            @RequestParam(name = "direction", defaultValue = "DESC") String direction
    ){
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page-1, Math.min(size,50), sort);
        return ResponseEntity.ok(new PageResponseWrapper<>(orderService.getAllOrders(pageable)));
    }

    @GetMapping("/me/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageResponseWrapper<OrderResponse>> getAllMyOrder(@AuthenticationPrincipal UserContext userContext,
                                                             @ModelAttribute OrderUserSearch search,
                                                             @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                             @RequestParam(name = "size", defaultValue = "5") Integer size,
                                                             @RequestParam(name = "sortBy", defaultValue = "createdAt")
                                                                 String sortBy,
                                                             @RequestParam(name = "direction", defaultValue = "DESC") String direction
    ){
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page-1, Math.min(size,50), sort);
        return ResponseEntity.ok(new PageResponseWrapper<>(orderService.getAllOrdersByAccountId(userContext.getAccountId(),pageable,search)));
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
