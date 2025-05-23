package com.taewoo.selfstudy.dto.memberDto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String name;
    private LocalDateTime createdAt;
}
