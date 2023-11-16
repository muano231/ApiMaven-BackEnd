package com.lterras.api.controller;

import com.lterras.api.convert.ProductConvert;
import com.lterras.api.dto.ProductDTO;
import com.lterras.api.model.Product;
import com.lterras.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductConvert productConvert;

    /**
     * Create - Post a new product
     * @param productDTO ProductDTO
     * @return object of a product
     */
    @PostMapping
    @PreAuthorize("hasRole('SUPPLIER')")
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        Product product = productConvert.productToEntity(productDTO);
        Product productCreated = productService.saveProduct(product);
        return productConvert.productToDTO(productCreated);
    }

    /**
     * Read - Get all products
     * @return iterable of product
     */
    @GetMapping
    public List<ProductDTO> getProducts() {
        return productService.getProducts().stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Read - Get product by id
     * @param id Long
     * @return product if exists
     */
    @GetMapping("/{id}")
    public ProductDTO getProduct(@PathVariable("id") final Long id) {
        return productConvert.productToDTO(productService.getProduct(id));
    }

    /**
     * Update - Update product by id
     * @param id Long
     * @param product Product
     * @return current product
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ProductDTO updateProduct(@PathVariable("id") final Long id, @RequestBody Product product) {
        Product p = productService.getProduct(id);
        if (p != null) {
            String description = product.getDescription();
            if (description != null) {
                p.setDescription(description);
            }
            String name = product.getName();
            if (name != null) {
                p.setName(name);
            }
            float price = product.getPrice();
            if (price != 0.0f) {
                p.setPrice(price);
            }
            int quantity = product.getQuantity();
            if (quantity != 0) {
                p.setQuantity(quantity);
            }

            productService.saveProduct(p);
            return productConvert.productToDTO(p);
        } else {
            return null;
        }
    }

    /**
     * Delete - Delete a product
     * @param id Long
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPPLIER')")
    public void deleteProduct(@PathVariable("id") final Long id) {
        productService.deleteProduct(id);
    }

}
