package com.lterras.api.service;

import com.lterras.api.model.Order;
import com.lterras.api.model.OrderDetails;
import com.lterras.api.repository.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDetailsService {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    public Iterable<OrderDetails> getAllOrderDetails() {
        return orderDetailsRepository.findAll();
    }

    public Iterable<OrderDetails> getAllOrderDetailsByOrderId(final Long id) {
        return orderDetailsRepository.findAllByOrderId(id);
    }

    public Iterable<OrderDetails> getAllOrderDetailsByOrder(Order order) {
        return orderDetailsRepository.findAllByOrder(order);
    }

    public OrderDetails getOrderDetails(final Long id) {
        return orderDetailsRepository.findById(id).orElse(null);
    }

    public OrderDetails saveOrderDetail(OrderDetails orderDetails) {
        return orderDetailsRepository.save(orderDetails);
    }

    public void deleteOrderDetail(final Long id) {
        orderDetailsRepository.deleteById(id);
    }

}