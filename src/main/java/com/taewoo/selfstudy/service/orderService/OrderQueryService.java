package com.taewoo.selfstudy.service.orderService;

import com.taewoo.selfstudy.dto.orderDto.OrderItemResponseDTO;
import com.taewoo.selfstudy.dto.orderDto.OrderResponseDTO;
import com.taewoo.selfstudy.entity.Member;
import com.taewoo.selfstudy.entity.OrderEntity;
import com.taewoo.selfstudy.exception.ResourceNotFoundException;
import com.taewoo.selfstudy.repository.OrderRepository;
import com.taewoo.selfstudy.service.memberService.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryService {

    private final OrderRepository orderRepository;
    private final MemberQueryService memberQueryService;

    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO getOrderById(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        return convertToDTO(order);
    }

    public List<OrderResponseDTO> getOrdersByMember(Long memberId) {
        Member member = memberQueryService.findMemberById(memberId);

        return member.getOrders().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
