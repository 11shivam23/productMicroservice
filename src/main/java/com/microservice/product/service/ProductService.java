package com.microservice.product.service;

import com.microservice.product.entity.Product;
import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();

    Product getProductById(Long productId);

    Product createProduct(Product product);

    Product updateProduct(Long productId, Product updatedProduct);

    void deleteProductById(Long productId);
}
