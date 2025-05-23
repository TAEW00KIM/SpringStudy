package com.taewoo.selfstudy.controller;

import com.taewoo.selfstudy.dto.orderDto.OrderRequestDTO;
import com.taewoo.selfstudy.dto.orderDto.OrderResponseDTO;
import com.taewoo.selfstudy.service.orderService.OrderCommandService;
import com.taewoo.selfstudy.service.orderService.OrderQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "상품 주문 API")
public class OrderController {

    private final OrderQueryService orderQueryService;
    private final OrderCommandService orderCommandService;

    @Operation(summary = "새 주문 생성", description = "상품 목록을 받아 새로운 주문을 생성합니다. 재고를 확인하고 차감합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "주문 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 재고 부족"),
            @ApiResponse(responseCode = "404", description = "주문할 상품을 찾을 수 없음")
    })
    @PostMapping("/api/members/{memberId}/orders")
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Parameter(description = "주문하는 회원의 ID", required = true) @PathVariable Long memberId,
            @RequestBody OrderRequestDTO orderRequestDto) {
        OrderResponseDTO createdOrder = orderCommandService.createOrder(memberId, orderRequestDto);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @Operation(summary = "특정 회원의 모든 주문 조회", description = "특정 회원의 모든 주문 목록을 조회합니다.")
    @GetMapping("/api/members/{memberId}/orders")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByMember(
            @Parameter(description = "주문 목록을 조회할 회원의 ID", required = true) @PathVariable Long memberId
    ) {
        List<OrderResponseDTO> orders = orderQueryService.getOrdersByMember(memberId);
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "모든 주문 조회", description = "시스템에 등록된 모든 주문 목록을 조회합니다.")
    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderQueryService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "특정 주문 조회 (ID)", description = "주문 ID를 이용하여 특정 주문의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 조회 성공"),
            @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음")
    })
    @GetMapping("/api/orders/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @Parameter(description = "조회할 주문의 ID", required = true) @PathVariable Long orderId) {
        OrderResponseDTO order = orderQueryService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
}
