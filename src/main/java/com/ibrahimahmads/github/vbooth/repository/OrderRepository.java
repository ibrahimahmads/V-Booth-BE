package com.ibrahimahmads.github.vbooth.repository;

import com.ibrahimahmads.github.vbooth.entity.Orders;
import com.ibrahimahmads.github.vbooth.utils.enums.OrderStatus;
import org.springframework.data.domain.Pageable;
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
    WHERE o.orderNumber = :orderNumbers
    AND o.status = :pendingStatus
""")
    void expireByOrderNumber(
            @Param("orderNumbers") List<String> orderNumbers,
            @Param("expiredStatus") OrderStatus expiredStatus,
            @Param("pendingStatus") OrderStatus pendingStatus
    );
    @Query("SELECT o.orderNumber FROM Orders o WHERE o.status = :status AND o.expiredAt <= :now ORDER BY o.expiredAt")
    List<String> getAllOrderNumberByStatusAndExpiredAtBefore(@Param("status") OrderStatus status, @Param("now") LocalDateTime expiredAt, Pageable pageable);
    @Query("SELECT o.orderNumber FROM Orders o WHERE o.status = :status AND o.expiredAt <= :now")
    List<String> getAllOrderNumberByStatusAndExpiredAtBefore(@Param("status") OrderStatus status, @Param("now") LocalDateTime expiredAt);
    List<Orders> findAllByAccountId(UUID accountId);
    Optional<Orders> findByIdAndAccountId(UUID id, UUID accountId);
    Optional<Orders> findByAccountIdAndPaketIdAndStatus(UUID accountId, UUID paketId, OrderStatus status);
    Optional<OrderStatus> getStatusByOrderNumber(String orderNumber);
    Optional<Orders> findByOrderNumberAndAccountId(String orderNumber, UUID accountId);
    Optional<Orders> findOrdersByOrderNumber(String orderNumber);
}
