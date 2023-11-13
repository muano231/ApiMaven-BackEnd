package com.lterras.api.convert;

import com.lterras.api.dto.ProductDTO;
import com.lterras.api.model.Product;
import com.lterras.api.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductConvert {

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    public ProductDTO productToDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }

    public Product productToEntity(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);

        if (productDTO.getId() != null) {
            Product oldProduct = productService.getProduct(productDTO.getId());
            product.setId(oldProduct.getId());
        }

        return product;
    }
}
