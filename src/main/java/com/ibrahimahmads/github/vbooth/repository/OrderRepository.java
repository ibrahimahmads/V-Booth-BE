package com.ibrahimahmads.github.vbooth.repository;

import com.ibrahimahmads.github.vbooth.entity.Orders;
import com.ibrahimahmads.github.vbooth.utils.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Orders, UUID>, JpaSpecificationExecutor<Orders> {
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
//    @EntityGraph(attributePaths = {"paket", "event"})
//    Page<Orders> findAllByAccountId(UUID accountId, Pageable pageable, Specification<Orders> specification );
    @Override
    @EntityGraph(attributePaths = {"paket", "event"})
    Page<Orders> findAll(Specification<Orders> spec, Pageable pageable);
    Optional<Orders> findByIdAndAccountId(UUID id, UUID accountId);
    Optional<Orders> findByAccountIdAndPaketIdAndStatus(UUID accountId, UUID paketId, OrderStatus status);
    @Query("SELECT o.status FROM Orders o WHERE o.orderNumber = :orderNumber")
    Optional<OrderStatus> getStatusByOrderNumber(@Param("orderNumber") String orderNumber);
    Optional<Orders> findByOrderNumberAndAccountId(String orderNumber, UUID accountId);
    Optional<Orders> findOrdersByOrderNumber(String orderNumber);
}
