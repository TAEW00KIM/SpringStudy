package com.taewoo.selfstudy.controller;

import com.taewoo.selfstudy.dto.memberDto.MemberResponseDTO;
import com.taewoo.selfstudy.dto.memberDto.MemberSignUpRequestDTO;
import com.taewoo.selfstudy.dto.productDto.ProductDTO;
import com.taewoo.selfstudy.service.memberService.MemberCommandService;
import com.taewoo.selfstudy.service.memberService.MemberQueryService;
import com.taewoo.selfstudy.service.productService.ProductLikeService;
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
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Member Management", description = "회원 관리 API")
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;
    private final ProductLikeService productLikeService;

    @Operation(summary = "회원 가입", description = "새로운 회원을 시스템에 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDTO> signUp(@RequestBody MemberSignUpRequestDTO signUpDTO) {
        MemberResponseDTO memberResponseDTO = memberCommandService.signUp(signUpDTO);
        return new ResponseEntity<>(memberResponseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "회원 정보 조회", description = "특정 회원의 정보를 조회합니다.")
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> getMemberInfo(@PathVariable Long memberId) {
        MemberResponseDTO memberResponseDTO = memberQueryService.getMemberInfo(memberId);
        return ResponseEntity.ok(memberResponseDTO);
    }

    @Operation(summary = "상품 좋아요 추가", description = "특정 회원이 특정 상픔에 좋아요를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "AUTH4001", description = "회원 토큰이 필요합니다.")
    })
    @PostMapping("/{memberId}/likes/{productId}")
    public ResponseEntity<Void> likeProduct(
            @Parameter(description = "회원 ID", required = true) @PathVariable Long memberId,
            @Parameter(description = "좋아요를 누를 상품 ID", required = true) @PathVariable Long productId
    ) {
        productLikeService.likeProduct(memberId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "상품 좋아요 취소", description = "상품에 추가한 좋아요를 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "AUTH4001", description = "회원 토큰이 필요합니다.")
    })
    @DeleteMapping("/{memberId}/likes/{productId}")
    public ResponseEntity<Void> unlikeProduct(
            @Parameter(description = "회원 ID", required = true) @PathVariable Long memberId,
            @Parameter(description = "좋아요를 취소할 상품 ID", required = true) @PathVariable Long productId
    ) {
        productLikeService.unlikeProduct(memberId, productId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "좋아요 한 상품 목록 조회", description = "좋아요를 누른 상품 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공"),
            @ApiResponse(responseCode = "AUTH4001", description = "회원 토큰이 필요합니다.")
    })
    @GetMapping("/{memberId}/likes")
    public ResponseEntity<List<ProductDTO>> getLikedProducts(
            @Parameter(description = "회원 ID", required = true) @PathVariable Long memberId
    ) {
        List<ProductDTO> likedProducts = productLikeService.getLikedProductsByMember(memberId);
        return ResponseEntity.ok(likedProducts);
    }
}
