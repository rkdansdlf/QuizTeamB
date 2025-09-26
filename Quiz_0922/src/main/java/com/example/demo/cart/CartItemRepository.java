package com.example.demo.cart;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  
  List<CartItem> findByUserId(String userId);
  Optional<CartItem> findByUserIdAndProductId(String userId, Long productId);
  void deleteByUserIdAndProductId(String userId, Long productId);
  void deleteByUserId(String userId);

}
