package com.taewoo.selfstudy.dto.orderDto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long orderId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmounts;
    private List<OrderItemResponseDTO> items;
}
