package com.lterras.api.dto;

import com.lterras.api.model.OrderDetails;
import lombok.Data;

@Data
public class OrderDetailsDTO {
    private Long id;

    //private Long order_id;

    private ProductDTO product;

    private float price;

    private int quantity;

    public OrderDetailsDTO() {}

    public OrderDetailsDTO(OrderDetails orderDetails) {
        this.setId(orderDetails.getId());
        //this.setOrder_id(orderDetails.getOrder().getId());
        this.setProduct(new ProductDTO(orderDetails.getProduct()));
        this.setPrice(orderDetails.getPrice());
        this.setQuantity(orderDetails.getQuantity());
    }
}
