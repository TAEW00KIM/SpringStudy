package com.taewoo.selfstudy.service.memberService;

import com.taewoo.selfstudy.dto.memberDto.MemberResponseDTO;
import com.taewoo.selfstudy.entity.Member;
import com.taewoo.selfstudy.exception.ResourceNotFoundException;
import com.taewoo.selfstudy.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public MemberResponseDTO getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 멤버가 존재하지 않습니다. " + memberId));
        return convertToDto(member);
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 멤버가 존재하지 않습니다." + memberId));
    }

    private MemberResponseDTO convertToDto(Member member) {
        return MemberResponseDTO.builder()
                .id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .name(member.getName())
                .createdAt(member.getCreatedAt())
                .build();
    }
}
