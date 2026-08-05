package com.shopverse.platform.productservice.service.impl;


import com.shopverse.platform.productservice.dto.ProductRequest;
import com.shopverse.platform.productservice.dto.ProductResponse;
import com.shopverse.platform.productservice.entity.Product;
import com.shopverse.platform.productservice.exception.ResourceNotFoundException;
import com.shopverse.platform.productservice.repository.ProductRepository;
import com.shopverse.platform.productservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger =
            LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        logger.info("Creating product: {}", request.getName());

        Product product = new Product();

        mapToEntity(request, product);

        Product savedProduct = productRepository.save(product);

        logger.info("Product created successfully with ID: {}", savedProduct.getId());

        return mapToResponse(savedProduct);
    }

    @Override
    public List<ProductResponse> getAllProducts() {

        logger.info("Fetching all products");

        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ProductResponse getProductById(Long id) {

        logger.info("Fetching product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with ID: " + id));

        return mapToResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        logger.info("Updating product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with ID: " + id));

        mapToEntity(request, product);

        Product updatedProduct = productRepository.save(product);

        logger.info("Product updated successfully with ID: {}", updatedProduct.getId());

        return mapToResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {

        logger.info("Deleting product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with ID: " + id));

        productRepository.delete(product);

        logger.info("Product deleted successfully with ID: {}", id);
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String category) {

        logger.info("Fetching products for category: {}", category);

        List<Product> products = productRepository.findByCategory(category);

        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> searchProducts(String name) {

        logger.info("Searching products with name: {}", name);

        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);

        return products.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private void mapToEntity(ProductRequest request, Product product) {

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(request.getCategory());
        product.setActive(request.getActive());
    }

    private ProductResponse mapToResponse(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .category(product.getCategory())
                .active(product.getActive())
                .build();
    }
}