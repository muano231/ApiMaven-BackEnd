package com.lterras.api.repository;

import com.lterras.api.model.Order;
import com.lterras.api.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    List<OrderDetails> findAllByOrderId(final Long id);

    List<OrderDetails> findAllByOrder(Order order);
}
