package com.taewoo.selfstudy.service.productService;

import com.taewoo.selfstudy.dto.productDto.ProductDTO;
import com.taewoo.selfstudy.entity.Product;
import com.taewoo.selfstudy.exception.ResourceNotFoundException;
import com.taewoo.selfstudy.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCommandService {
    private final ProductRepository productRepository;

    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .stock(productDTO.getStock())
                .build();

        Product saveProduct = productRepository.save(product);

        return ProductDTO.builder()
                .id(saveProduct.getId())
                .name(saveProduct.getName())
                .description(saveProduct.getDescription())
                .price(saveProduct.getPrice())
                .stock(saveProduct.getStock())
                .build();
    }

    @Transactional
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found with Id:  " + productId));

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());

        Product updateProduct = productRepository.save(product);

        return ProductDTO.builder()
                .id(updateProduct.getId())
                .name(updateProduct.getName())
                .description(updateProduct.getDescription())
                .price(updateProduct.getPrice())
                .stock(updateProduct.getStock())
                .build();
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found with Id:  " + productId));
        productRepository.delete(product);
    }
}
