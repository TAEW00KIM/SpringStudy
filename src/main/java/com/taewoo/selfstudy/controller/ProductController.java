package com.taewoo.selfstudy.controller;

import com.taewoo.selfstudy.dto.productDto.ProductDTO;
import com.taewoo.selfstudy.service.productService.ProductCommandService;
import com.taewoo.selfstudy.service.productService.ProductQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "상품 관리 API")
public class ProductController {
    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;

    @Operation(summary = "새 상품 등록", description = "새로운 상품 정보를 시스템에 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "상품 등록 성공",
            content = @Content(schema = @Schema(implementation = ProductDTO.class))),
            @ApiResponse(responseCode = "PRODUCT4001", description = "잘못된 요청")
    })
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createProduct = productCommandService.createProduct(productDTO);
        return new ResponseEntity<>(createProduct, HttpStatus.CREATED);
    }

    @Operation(summary = "모든 상품 조회", description = "등록된 모든 상품을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productQueryService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "특정 상품 조회", description = "상품 ID를 이용해 특정 상품을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "PRODUCT4001", description = "잘못된 요청")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productQueryService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "상품 정보 수정", description = "상품 ID를 이용해 특정 상품 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "PRODUCT4001", description = "잘못된 요청"),
            @ApiResponse(responseCode = "PRODUCT4002", description = "상품을 찾을 수 없습니다.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @Parameter(description = "수정할 상품의 ID", required = true) @PathVariable Long id,
            @RequestBody ProductDTO product) {
        ProductDTO updateProduct = productCommandService.updateProduct(id, product);
        return ResponseEntity.ok(updateProduct);
    }

    @Operation(summary = "상품 삭제", description = "상품 ID를 이용하여 특정 상품을 시스템에서 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "상품 삭제 성공 (내용 없음)"),
            @ApiResponse(responseCode = "PRODUCT4002", description = "삭제할 상품을 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "삭제할 상품의 ID", required = true) @PathVariable Long id) {
        productCommandService.deleteProduct(id);
        return ResponseEntity.noContent().build(); // 204 No Content 상태 코드 응답
    }
}
