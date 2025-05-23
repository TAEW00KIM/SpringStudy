package com.taewoo.selfstudy.dto.orderDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private List<OrderItemRequestDTO> items;
}
