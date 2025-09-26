package com.example.demo.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor 
@AllArgsConstructor //객체 생성 시 모든 필드를 초기화해야 하는 경우에 사용
public class CartItemDto {
    private Long productId;
    private String productName;
    private String memoryIcon;
    private String originalOwner;
    private Integer price;
    private Integer quantity;
    private Integer stock;
    private Integer rarityScore;
    private String description;
    
    // 계산 필드
    public Integer getTotalPrice() {
        return price != null && quantity != null ? price * quantity : 0;
    }
}
