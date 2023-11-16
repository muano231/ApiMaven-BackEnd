package com.lterras.api.dto;

import com.lterras.api.model.Role;
import com.lterras.api.model.User;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserDTO {

    private Long id;

    private String email;

    private String username;

    private Set<Role> roles;

    private List<OrderDTO> orders;

    public UserDTO() {}

    public UserDTO(User user) {
        this.setId(user.getId());
        this.setEmail(user.getEmail());
        this.setUsername(user.getUsername());
        this.setRoles(user.getRoles());
        List<OrderDTO> orderDTOs = user.getOrders().stream().map(OrderDTO::new).collect(Collectors.toList());
        this.setOrders(orderDTOs);
    }

}
