package com.microservice.product.service;

import com.microservice.product.entity.Product;
import com.microservice.product.exception.ConcurrencyConflictException;
import com.microservice.product.repository.ProductRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public Product createProduct(Product product) {
        // Set initial version to 1
        product.setVersion(1);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long productId, Product updatedProduct) {
        try {
            Product existingProduct = productRepository.findById(productId).orElse(null);
            if(existingProduct!=null && existingProduct.getVersion() == updatedProduct.getVersion()) {

                existingProduct.setProductName(updatedProduct.getProductName());
                existingProduct.setPrice(updatedProduct.getPrice());
                existingProduct.setQuantity(updatedProduct.getQuantity());

                return productRepository.save(existingProduct);
            } else {
                throw new ConcurrencyConflictException("Product update failed due to concurrency conflict.");
            }
        } catch (OptimisticLockException ex) {
            throw new RuntimeException("Product update failed. ", ex);
        }
    }

    public void deleteProductById(Long productId) {
        productRepository.deleteById(productId);
    }

}

