package com.example.demo.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProductService productService;

    // 상품 목록 조회 (필터링, 페이징, 정렬 포함)
    @GetMapping
    public ResponseEntity<Page<ProductDto.ProductListDto>> getProducts(
    		@PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "memoryType",required = false) String memoryType,
            @RequestParam(name = "price",required = false) String price,
            @RequestParam(name = "owner",required = false) String owner,
            @RequestParam(name = "emotion",required = false) Integer emotion
    ) {
        Page<ProductDto.ProductListDto> products = productService.getProducts(pageable, search, memoryType, price, owner, emotion);
        return ResponseEntity.ok(products);
    }

    // 상품 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto.ProductDetailDto> getProductById(@PathVariable("id") Long id) {
        ProductDto.ProductDetailDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    // 추천 상품 조회 (메인 페이지용)
    @GetMapping("/featured")
    public ResponseEntity<List<ProductDto.FeaturedProductDto>> getFeaturedProducts() {
        List<ProductDto.FeaturedProductDto> featuredProducts = productService.getFeaturedProducts();
        return ResponseEntity.ok(featuredProducts);
    }
}