package com.ibrahimahmads.github.vbooth.controller;

import com.ibrahimahmads.github.vbooth.dto.response.CheckoutResponse;
import com.ibrahimahmads.github.vbooth.entity.Orders;
import com.ibrahimahmads.github.vbooth.security.model.UserContext;
import com.ibrahimahmads.github.vbooth.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/checkout/qris")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CheckoutResponse> checkoutWithQris(Authentication authentication, @RequestParam UUID paket){
        if(authentication.getPrincipal() == null ) throw new NullPointerException("login first to access this resource");
        UserContext userContext = (UserContext) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrderByQris(userContext.getAccountId(),paket));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Orders>> getAllOrder(){
        return ResponseEntity.ok().body(orderService.getAllOrders());
    }

    @GetMapping("/me/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Orders>> getAllMyOrder(Authentication authentication){
        if(authentication.getPrincipal() == null ) throw new NullPointerException("login first to access this resource");
        UserContext userContext = (UserContext) authentication.getPrincipal();
        return ResponseEntity.ok().body(orderService.getAllOrdersByAccountId(userContext.getAccountId()));
    }
}
