package com.lterras.api.convert;

import com.lterras.api.dto.OrderDTO;
import com.lterras.api.dto.OrderDetailsDTO;
import com.lterras.api.model.Order;
import com.lterras.api.model.OrderDetails;
import com.lterras.api.service.OrderDetailsService;
import com.lterras.api.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderConvert {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailsService orderDetailsService;

    @Autowired
    private ModelMapper modelMapper;

    public OrderDTO orderToDTO(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }

    public Order orderToEntity(OrderDTO orderDTO) {
        Order order = modelMapper.map(orderDTO, Order.class);

        if(orderDTO.getId() != null) {
            Order oldOrder = orderService.getOrder(orderDTO.getId());
            order.setId(oldOrder.getId());
        }
        return order;
    }

    public OrderDetailsDTO orderDetailsToDTO(OrderDetails orderDetails) {
        return modelMapper.map(orderDetails, OrderDetailsDTO.class);
    }

    public OrderDetails orderDetailsToEntity(OrderDetailsDTO orderDetailsDTO) {
        OrderDetails orderDetails = modelMapper.map(orderDetailsDTO, OrderDetails.class);

        if(orderDetailsDTO.getId() != null) {
            OrderDetails oldOrderDetails = orderDetailsService.getOrderDetails(orderDetailsDTO.getId());
            orderDetails.setId(oldOrderDetails.getId());
        }
        return orderDetails;
    }
}
