package com.lterras.api.controller;

import com.lterras.api.convert.UserConvert;
import com.lterras.api.dto.UserDTO;
import com.lterras.api.model.ERole;
import com.lterras.api.model.Role;
import com.lterras.api.model.User;
import com.lterras.api.payload.response.MessageResponse;
import com.lterras.api.repository.RoleRepository;
import com.lterras.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConvert userConvert;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.getUsers().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable("id") final Long id) {
        return userConvert.userToDTO(userService.getUser(id));
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable("id") final Long id, @RequestBody User user) {
        User u = userService.getUser(id);
        if(u != null) {
            String email = user.getEmail();
            if (email != null) {
                u.setEmail(email);
            }
            String password = user.getPassword();
            if (password != null) {
                u.setPassword(encoder.encode(password));
            }
            String username = user.getUsername();
            if (username != null) {
                u.setUsername(username);
            }

            userService.saveUser(u);
            return userConvert.userToDTO(u);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") final Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}/roles")
    public Set<Role> getUsersRoles(@PathVariable("id") final Long id) {
        return userService.getUser(id).getRoles();
    }

    /*
    @PutMapping("/{id}/roles")
    public Set<Role> updateUsersRole(@PathVariable("id") final Long id, @RequestBody Set<String> roles) {
        User user = userService.getUser(id);

        Set<String> strRoles = roles;
        Set<Role> r = new HashSet<>();

        if (strRoles == null) {
            Role customerRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            r.add(customerRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        r.add(adminRole);

                        break;
                    case "supplier":
                        Role supplierRole = roleRepository.findByName(ERole.ROLE_SUPPLIER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        r.add(supplierRole);

                        break;
                    default:
                        Role customerRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        r.add(customerRole);

                        break;
                }
            });
        }
        user.setRoles(r);
        userService.saveUser(user);

        return r;
    }*/

}
