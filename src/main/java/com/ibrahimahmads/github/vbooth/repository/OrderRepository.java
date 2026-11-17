package com.ibrahimahmads.github.vbooth.repository;

import com.ibrahimahmads.github.vbooth.entity.Orders;
import com.ibrahimahmads.github.vbooth.utils.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Orders, UUID> {
    @Modifying(clearAutomatically = true)
    @Query("""
    UPDATE Orders o
    SET o.status = :expiredStatus
    WHERE o.status = :pendingStatus
    AND o.expiredAt <= :now
""")
    void expirePendingOrders(
            @Param("pendingStatus") OrderStatus pendingStatus,
            @Param("expiredStatus") OrderStatus expiredStatus,
            @Param("now") LocalDateTime now
    );
    List<Orders> findAllByAccountId(UUID accountId);
    Optional<Orders> findByIdAndAccountId(UUID id, UUID accountId);
    Optional<Orders> findByAccountIdAndPaketIdAndStatus(UUID accountId, UUID paketId, OrderStatus status);
    Optional<OrderStatus> getStatusByOrderNumber(String orderNumber);
    Optional<Orders> findByOrderNumberAndAccountId(String orderNumber, UUID accountId);
    Optional<Orders> findOrdersByOrderNumber(String orderNumber);
}
