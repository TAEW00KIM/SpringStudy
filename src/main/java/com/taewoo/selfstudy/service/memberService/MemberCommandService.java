package com.taewoo.selfstudy.service.memberService;

import com.taewoo.selfstudy.dto.memberDto.MemberResponseDTO;
import com.taewoo.selfstudy.dto.memberDto.MemberSignUpRequestDTO;
import com.taewoo.selfstudy.entity.Member;
import com.taewoo.selfstudy.exception.DuplicateResourceException;
import com.taewoo.selfstudy.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponseDTO signUp(MemberSignUpRequestDTO signUpDto) {
        if (memberRepository.existsByUsername(signUpDto.getUsername())) {
            throw new DuplicateResourceException("이미 사용 중인 사용자 이름입니다. " + signUpDto.getUsername());
        }

        if (memberRepository.existsByEmail(signUpDto.getEmail())) {
            throw new DuplicateResourceException("이미 사용 중인 이메일입니다. " + signUpDto.getEmail());
        }

        Member member = Member.builder()
                .username(signUpDto.getUsername())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .email(signUpDto.getEmail())
                .name(signUpDto.getName())
                .build();

        Member savedMember = memberRepository.save(member);
        return convertToDTO(savedMember);
    }

    private MemberResponseDTO convertToDTO(Member member) {
        return MemberResponseDTO.builder()
                .id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .name(member.getName())
                .createdAt(member.getCreatedAt())
                .build();
    }
}
