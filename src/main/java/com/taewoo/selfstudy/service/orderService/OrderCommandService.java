package com.taewoo.selfstudy.service.orderService;

import com.taewoo.selfstudy.dto.orderDto.OrderItemRequestDTO;
import com.taewoo.selfstudy.dto.orderDto.OrderItemResponseDTO;
import com.taewoo.selfstudy.dto.orderDto.OrderRequestDTO;
import com.taewoo.selfstudy.dto.orderDto.OrderResponseDTO;
import com.taewoo.selfstudy.entity.Member;
import com.taewoo.selfstudy.entity.OrderEntity;
import com.taewoo.selfstudy.entity.OrderItem;
import com.taewoo.selfstudy.entity.Product;
import com.taewoo.selfstudy.exception.InsufficientStockException;
import com.taewoo.selfstudy.exception.ResourceNotFoundException;
import com.taewoo.selfstudy.repository.OrderRepository;
import com.taewoo.selfstudy.repository.ProductRepository;
import com.taewoo.selfstudy.service.memberService.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderCommandService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final MemberQueryService memberQueryService;

    @Transactional
    public OrderResponseDTO createOrder(Long memberId, OrderRequestDTO requestDTO) {
        Member member = memberQueryService.findMemberById(memberId);

        List<OrderItem> tempOrderItems = new ArrayList<>();
        BigDecimal calculatedTotalAmount = BigDecimal.ZERO;

        OrderEntity newOrder = OrderEntity.builder().member(member).build();

        for (OrderItemRequestDTO itemDto : requestDTO.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + itemDto.getProductId()));

            if (product.getStock() < itemDto.getQuantity()) {
                throw new InsufficientStockException("Not enough stock for product " + product.getName() +
                        ", Available: " + product.getStock() +
                        ", Requested: " + itemDto.getQuantity());
            }

            OrderItem orderItem = OrderItem.builder()
                    .order(newOrder)
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .priceAtOrder(product.getPrice())
                    .build();

            tempOrderItems.add(orderItem);

            calculatedTotalAmount = calculatedTotalAmount.add(
                    product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()))
            );
            product.setStock(product.getStock() - itemDto.getQuantity());
            productRepository.save(product);
        }

        newOrder.setTotalAmount(calculatedTotalAmount);
        tempOrderItems.forEach(newOrder::addOrderItem);
        OrderEntity savedOrder = orderRepository.save(newOrder);

        return convertToDTO(savedOrder);
    }

    private OrderResponseDTO convertToDTO(OrderEntity order) {
        List<OrderItemResponseDTO> itemDtos = order.getOrderItems().stream()
                .map(item -> OrderItemResponseDTO.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .priceAtOrder(item.getPriceAtOrder())
                        .build())
                .collect(Collectors.toList());


        return OrderResponseDTO.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .totalAmounts(order.getTotalAmount())
                .items(itemDtos)
                .build();
    }
}
