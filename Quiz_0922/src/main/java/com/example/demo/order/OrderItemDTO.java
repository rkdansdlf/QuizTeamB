package com.example.demo.order;

import com.example.demo.product.Product;
import com.example.demo.product.ProductDto.ProductDetailDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@RequiredArgsConstructor
public class OrderItemDTO {
	
	 private ProductDetailDto product; 
	 
	 private int quantity;    
	
	 public OrderItemDTO(ProductDetailDto productInfo, int quantity) {
		 this.product = productInfo;
	     this.quantity = quantity;
	 }
	 
}
