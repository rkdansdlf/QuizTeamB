package com.example.demo.cart;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class CartSummaryDto {
    private List<CartItemDto> items;
    private Integer subtotal;
    private Integer shippingFee;
    private Integer total;
    private Integer totalItems;
    
    public static CartSummaryDto create(List<CartItemDto> items) {
        Integer subtotal = items.stream()
            .mapToInt(CartItemDto::getTotalPrice)
            .sum();
        Integer shippingFee = subtotal >= 50000 ? 0 : 3000;
        Integer totalItems = items.stream()
            .mapToInt(CartItemDto::getQuantity)
            .sum();
        
        return CartSummaryDto.builder()
            .items(items)
            .subtotal(subtotal)
            .shippingFee(shippingFee)
            .total(subtotal + shippingFee)
            .totalItems(totalItems)
            .build();
    }
}