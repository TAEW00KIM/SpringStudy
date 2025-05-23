package com.taewoo.selfstudy.dto.memberDto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignUpRequestDTO {
    private String username;
    private String password;
    private String email;
    private String name;
}
