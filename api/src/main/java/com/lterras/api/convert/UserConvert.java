package com.lterras.api.convert;

import com.lterras.api.dto.UserDTO;
import com.lterras.api.model.User;
import com.lterras.api.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConvert {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    public UserDTO userToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public User productToEntity(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);

        if (userDTO.getId() != null) {
            User oldUser = userService.getUser(userDTO.getId());
            user.setId(oldUser.getId());
        }

        return user;
    }

}
