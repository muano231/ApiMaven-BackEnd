package com.lterras.api.dto;

import com.lterras.api.model.EStatus;
import com.lterras.api.model.Order;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderDTO {

    private Long id;

    private float price;

    private EStatus status;

    private List<OrderDetailsDTO> orderDetails;


    public OrderDTO() {
    }

    public OrderDTO(Order order) {
        this.setId(order.getId());
        this.setPrice(order.getPrice());
        this.setStatus(order.getStatus().getName());
        List<OrderDetailsDTO> orderDetailsDTOs = order.getOrderDetails().stream().map(OrderDetailsDTO::new).collect(Collectors.toList());
        this.setOrderDetails(orderDetailsDTOs);
    }
}
