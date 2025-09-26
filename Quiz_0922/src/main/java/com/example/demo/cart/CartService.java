package com.example.demo.cart;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    
    private final CartItemRepository cartItemRepository;
    // private final ProductRepository productRepository;
    
    public void addToCart(String userId, Long productId, Integer quantity) {
        // 기존 장바구니 아이템 확인
        Optional<CartItem> existingItem = cartItemRepository
            .findByUserIdAndProductId(userId, productId);
        
        if (existingItem.isPresent()) {
            // 수량 증가
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            // 새 아이템 추가
            CartItem newItem = CartItem.builder()
                .userId(userId)
                .productId(productId)
                .quantity(quantity)
                .build();
            cartItemRepository.save(newItem);
        }
    }
     	
    // public CartSummaryDto getCartSummary(String userId) {
    //     List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        
    //     List<CartItemDto> itemDtos = cartItems.stream()
    //         .map(this::convertToDto)
    //         .collect(Collectors.toList());
        
    //     return CartSummaryDto.create(itemDtos);
    // }
    
    public void updateQuantity(String userId, Long productId, Integer quantity) {
        
    	CartItem item = cartItemRepository
            .findByUserIdAndProductId(userId, productId)
            .orElseThrow(() -> new RuntimeException("장바구니 아이템을 찾을 수 없습니다"));
        
        
        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }
    
    public void removeItem(String userId, Long productId) {
        cartItemRepository.deleteByUserIdAndProductId(userId, productId);
    }
    
    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
    
    public Integer getCartItemCount(String userId) {
        return cartItemRepository.findByUserId(userId)
            .stream()
            .mapToInt(CartItem::getQuantity)
            .sum();
    }
    
    // private CartItemDto convertToDto(CartItem cartItem) {
    //     Product product = ProductRepository.findById(cartItem.getProductId())
    //         .orElse(null);
        
    //     if (product == null) {
    //         return null;
    //     }
        
    //     return CartItemDto.builder()
    //         .productId(product.getId())
    //         .productName(product.getName())
    //         .memoryIcon(getMemoryTypeIcon(product.getMemoryType()))
    //         .originalOwner(product.getOriginalOwner())
    //         .price(product.getPrice())
    //         .quantity(cartItem.getQuantity())
    //         .stock(product.getStock())
    //         .rarityScore(product.getRarityScore())
    //         .description(product.getDescription())
    //         .build();
    // }
    
    private String getMemoryTypeIcon(String memoryType) {
        Map<String, String> icons = Map.of(
            "CHILDHOOD", "🧸",
            "FRIENDSHIP", "👫",
            "LOVE", "💕",
            "ADVENTURE", "🗺️",
            "FOOD", "🍰"
//             "TOY", "🎮",
//             "EXPERIMENT", "🧪"
        );
        return icons.getOrDefault(memoryType, "🎁");
    }

    public CartSummaryDto getCartSummary(String userId) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("요약 데이터 에러");
    }
}