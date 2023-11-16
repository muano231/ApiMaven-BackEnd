package com.lterras.api.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lterras.api.convert.OrderConvert;
import com.lterras.api.convert.UserConvert;
import com.lterras.api.dto.OrderDTO;
import com.lterras.api.dto.OrderDetailsDTO;
import com.lterras.api.dto.ProductDTO;
import com.lterras.api.dto.UserDTO;
import com.lterras.api.model.*;
import com.lterras.api.repository.ProductRepository;
import com.lterras.api.repository.RoleRepository;
import com.lterras.api.repository.StatusRepository;
import com.lterras.api.service.OrderDetailsService;
import com.lterras.api.service.OrderService;
import com.lterras.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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

    @Autowired
    private OrderConvert orderConvert;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailsService orderDetailsService;

    /**
     * Read - Get all users
     * @return List of users
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getUsers() {
        return userService.getUsers().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Read - Get user by id
     * @param user_id
     * @return user
     */
    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable("id") final Long user_id) {
        return userConvert.userToDTO(userService.getUser(user_id));
    }

    /**
     * Update - Update user's information
     * @param user_id
     * @param user
     * @return user
     */
    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable("id") final Long user_id, @RequestBody User user) {
        User u = userService.getUser(user_id);
        if(u != null) {
            String email = user.getEmail();
            if (email != null) {
                u.setEmail(email);
            }
            String password = user.getPassword();
            if (password != null) {
                u.setPassword(encoder.encode(password));
            }
            /*
            String username = user.getUsername();
            if (username != null) {
                u.setUsername(username);
            }*/

            userService.saveUser(u);
            return userConvert.userToDTO(u);
        } else {
            return null;
        }
    }

    /**
     * Delete - Delete a user
     * @param user_id
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable("id") final Long user_id) {
        userService.deleteUser(user_id);
    }

    /**
     * Read - Get user's roles
     * @param user_id
     * @return Set of roles
     */
    @GetMapping("/{id}/roles")
    public Set<Role> getUsersRoles(@PathVariable("id") final Long user_id) {
        return userService.getUser(user_id).getRoles();
    }

    /**
     * Create - Post a new order
     * @param user_id
     * @param objectNode
     * @return order
     */
    @PostMapping("/{id}/orders")
    @PreAuthorize("hasRole('CUSTOMER')")
    public OrderDTO newUsersOrder(@PathVariable("id") final Long user_id, @RequestBody ObjectNode objectNode) {
        // Order informations
        User user = userService.getUser(user_id);
        Long product_id = objectNode.get("product_id").longValue();
        int quantity = objectNode.get("quantity").intValue();

        // Récupération du produit
        Product product = productRepository.getReferenceById(product_id);

        // Création de la commande
        Order order = new Order(user,
                product.getPrice() * quantity,
                statusRepository.findByName(EStatus.STATUS_IN_CART));

        // Ajout de la commande en base de données
        orderService.saveOrder(order);

        // Création du détail de la commande
        OrderDetails orderDetails = new OrderDetails(order,
                product,
                quantity);

        // Ajout du détail de la commande en base de données
        orderDetailsService.saveOrderDetail(orderDetails);

        // Renvoie du DTO de la commande
        return orderConvert.orderToDTO(order);
    }

    /**
     * Read - Get user's orders
     * @param user_id
     * @return List of orders
     */
    @GetMapping("/{id}/orders")
    public List<OrderDTO> getUsersOrders(@PathVariable("id") final Long user_id) {
        return userService.getUser(user_id).getOrders().stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Read - Get user's order details
     * @param user_id
     * @param order_id
     * @return Order
     */
    @GetMapping("/{id}/orders/{secondId}")
    public OrderDTO getUsersOrder(@PathVariable("id") final Long user_id, @PathVariable("secondId") final Long order_id) {
        User user = userService.getUser(user_id);

        if (user != null) {
            Optional<Order> optionalOrder = user.getOrders().stream()
                    .filter(order -> order.getId().equals(order_id))
                    .findFirst();

            if (optionalOrder.isPresent()) {
                return new OrderDTO(optionalOrder.get());
            }
        }

        return null;
    }

    /**
     * Update - Update user's order status
     * @param user_id
     * @param order_id
     * @param status_name
     * @return Order
     */
    @PutMapping("/{id}/orders/{secondId}")
    public OrderDTO updateUsersOrderStatus(@PathVariable("id") final Long user_id,
                                           @PathVariable("secondId") final Long order_id,
                                           @RequestBody String status_name) {
        User user = userService.getUser(user_id);

        if (user != null) {
            Optional<Order> optionalOrder = user.getOrders().stream()
                    .filter(order -> order.getId().equals(order_id))
                    .findFirst();

            if (optionalOrder.isPresent()) {

                Status status = new Status();

                switch (status_name) {
                    case "pending":
                        status = statusRepository.findByName(EStatus.STATUS_PENDING);
                        break;
                    case "in progress":
                        status = statusRepository.findByName(EStatus.STATUS_IN_PROGRESS);
                        break;
                    case "delivered":
                        status = statusRepository.findByName(EStatus.STATUS_DELIVERED);
                        break;
                    case "cancelled":
                        status = statusRepository.findByName(EStatus.STATUS_CANCELLED);
                        break;
                    case "refund":
                        status = statusRepository.findByName(EStatus.STATUS_REFUND);
                        break;
                    default:
                        break;
                }
                optionalOrder.get().setStatus(status);
                Order order = optionalOrder.get();
                orderService.saveOrder(order);
                return new OrderDTO(order);
            }
        }

        return null;
    }

    /**
     * Delete - Delete user's order
     * @param user_id
     * @param order_id
     */
    @DeleteMapping("/{id}/orders/{secondId}")
    public void deleteUsersOrder(@PathVariable("id") final Long user_id, @PathVariable("secondId") final Long order_id) {
        User user = userService.getUser(user_id);

        if (user != null) {
            Optional<Order> optionalOrder = user.getOrders().stream()
                    .filter(order -> order.getId().equals(order_id))
                    .findFirst();

            optionalOrder.ifPresent(order -> orderService.deleteOrder(order.getId()));
        }
    }

    /**
     * Read - Get all user's order's details
     * @param user_id
     * @param order_id
     * @return List of order details
     */
    @GetMapping("/{id}/orders/{secondId}/details")
    public List<OrderDetailsDTO> getUsersOrderDetails(@PathVariable("id") final Long user_id, @PathVariable("secondId") final Long order_id) {
        User user = userService.getUser(user_id);

        if (user != null) {
            Optional<Order> optionalOrder = user.getOrders().stream()
                    .filter(order -> order.getId().equals(order_id))
                    .findFirst();

            if (optionalOrder.isPresent()) {
                OrderDTO orderDTO = new OrderDTO(optionalOrder.get());
                Order order = orderConvert.orderToEntity(orderDTO);
                return order.getOrderDetails().stream()
                        .map(OrderDetailsDTO::new)
                        .toList();
            }
        }

        return null;
    }

    /**
     * Read - Get user's order's detail
     * @param user_id
     * @param order_id
     * @param order_details_id
     * @return OrderDetails
     */
    @GetMapping("/{id}/orders/{secondId}/details/{thirdId}")
    public OrderDetailsDTO getUsersOrderDetails(@PathVariable("id") final Long user_id,
                                                      @PathVariable("secondId") final Long order_id,
                                                      @PathVariable("thirdId") final Long order_details_id) {
        User user = userService.getUser(user_id);

        if (user != null) {
            Optional<Order> optionalOrder = user.getOrders().stream()
                    .filter(order -> order.getId().equals(order_id))
                    .findFirst();

            if (optionalOrder.isPresent()) {
                OrderDTO orderDTO = new OrderDTO(optionalOrder.get());
                Order order = orderConvert.orderToEntity(orderDTO);

                Optional<OrderDetails> optionalOrderDetails = order.getOrderDetails().stream()
                        .filter(orderDetails -> orderDetails.getId().equals(order_details_id))
                        .findFirst();

                return optionalOrderDetails.map(OrderDetailsDTO::new).orElse(null);


            }
        }

        return null;
    }

    /**
     * Read - Get user's order's detail's product
     * @param user_id
     * @param order_id
     * @param order_details_id
     * @return Product
     */
    @GetMapping("/{id}/orders/{secondId}/details/{thirdId}/product")
    public ProductDTO getUsersOrderDetailsProduct(@PathVariable("id") final Long user_id,
                                                  @PathVariable("secondId") final Long order_id,
                                                  @PathVariable("thirdId") final Long order_details_id) {
        User user = userService.getUser(user_id);

        if (user != null) {
            Optional<Order> optionalOrder = user.getOrders().stream()
                    .filter(order -> order.getId().equals(order_id))
                    .findFirst();

            if (optionalOrder.isPresent()) {
                OrderDTO orderDTO = new OrderDTO(optionalOrder.get());
                Order order = orderConvert.orderToEntity(orderDTO);

                Optional<OrderDetails> optionalOrderDetails = order.getOrderDetails().stream()
                        .filter(orderDetails -> orderDetails.getId().equals(order_details_id))
                        .findFirst();

                OrderDetailsDTO orderDetailsDTO = optionalOrderDetails.map(OrderDetailsDTO::new).orElse(null);

                if (orderDetailsDTO != null) {
                    return orderDetailsDTO.getProduct();
                }
            }
        }

        return null;
    }

}
