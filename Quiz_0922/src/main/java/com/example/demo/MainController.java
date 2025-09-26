package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.product.ProductDto;
import com.example.demo.product.ProductService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MainController {

	private final ProductService productService;
	
	// foward:/ -> 타입리프가 아니라 static에 html을 옮겼고 js랑 연동해서 사용하려면 
	// forward:/를 사용하라고 해서 사용함
    @GetMapping("/products")
    public String showProductsPage() {
        return "forward:/products.html";
    }

    
    @GetMapping("/")
    public String showIndexPage() {
        return "forward:/index.html";
    }
   
 
    @GetMapping("/products/{id}")
    public String showProductDetailPage(@PathVariable("id") Long id, Model model) {
        ProductDto.ProductDetailDto productData = productService.getProductById(id);
        model.addAttribute("product", productData);
        return "product-detail";
    }
    
//    @GetMapping("/cart")
//    public String showCartPage() {
//        return "forward:/cart.html";
//    }
}


