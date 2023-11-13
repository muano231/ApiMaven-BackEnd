package com.lterras.api.dto;

import com.lterras.api.model.Role;
import com.lterras.api.model.User;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserDTO {

    private Long id;

    private String email;

    private String username;

    private Set<Role> roles;

    public UserDTO() {}

    public UserDTO(User user) {
        this.setId(user.getId());
        this.setEmail(user.getEmail());
        this.setUsername(user.getUsername());
        this.setRoles(user.getRoles());
    }

}
