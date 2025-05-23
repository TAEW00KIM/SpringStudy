package com.taewoo.selfstudy.service.productService;

import com.taewoo.selfstudy.dto.productDto.ProductDTO;
import com.taewoo.selfstudy.entity.Member;
import com.taewoo.selfstudy.entity.Product;
import com.taewoo.selfstudy.service.memberService.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductLikeService {

    private final MemberQueryService memberQueryService;
    private final ProductQueryService productQueryService;
    private final ProductCommandService productCommandService;

    public void likeProduct(Long memberId, Long productId) {
        Member member = memberQueryService.findMemberById(memberId);
        Product product = productQueryService.findProductEntityById(productId);

        /* 상품에 이미 좋아요를 누른 경우 */
        if (member.getLikedProducts().contains(product)) {
            return;
        }

        member.likeProduct(product);
    }

    public void unlikeProduct(Long memberId, Long productId) {
        Member member = memberQueryService.findMemberById(memberId);
        Product product = productQueryService.findProductEntityById(productId);

        member.unlikeProduct(product);
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getLikedProductsByMember(Long memberId) {
        Member member = memberQueryService.findMemberById(memberId);
        Set<Product> likedProducts = member.getLikedProducts();

        return likedProducts.stream()
                .map(product -> productQueryService.convertToDTO(product))
                .collect(Collectors.toList());
    }
}
