package com.example.demo.cart;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartApiController {
    
    private final CartService cartService;
    
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody @Valid AddToCartRequest request, 
                                     HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        cartService.addToCart(userId, request.getProductId(), request.getQuantity());
        
        Map<String, Object> response = Map.of(
            "success", true,
            "message", "장바구니에 추가되었습니다! 🛒",
            "cartItemCount", cartService.getCartItemCount(userId)
        );
        
        return ResponseEntity.ok(response);
    }
    
//    수량 변경
    @PutMapping("/item/{productId}")
    public ResponseEntity<?> updateQuantity(@PathVariable Long productId,
                                          @RequestParam Integer quantity,
                                          HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        cartService.updateQuantity(userId, productId, quantity);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "cartItemCount", cartService.getCartItemCount(userId)
        ));
    }
//    localStorage에 저장되어있는 api 삭제
    @DeleteMapping("/item/{productId}")
    public ResponseEntity<?> removeItem(@PathVariable Long productId,
                                      HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        cartService.removeItem(userId, productId);
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "상품이 제거되었습니다.",
            "cartItemCount", cartService.getCartItemCount(userId)
        ));
    }
    
    // 전체삭제
    // @DELETE("/clear")
    // public ResponseEntity<?> clearCart(HttpSession session) {
    //     String userId = (String) session.getAttribute("userId");
    //     if (userId == null) {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    //     }
        
    //     cartService.clearCart(userId);
        
    //     return ResponseEntity.ok(Map.of(
    //         "success", true,
    //         "message", "장바구니 비우기"",
    //         "cartItemCount", 0
    //     ));
    // }
}