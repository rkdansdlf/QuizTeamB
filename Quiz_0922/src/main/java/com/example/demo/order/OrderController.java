package com.example.demo.order;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.product.Product;
import com.example.demo.product.ProductDto.ProductDetailDto;
import com.example.demo.product.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {
	
	private OrderService orderService;
	private ProductService productService;
	
	@PostMapping("/order/direct")
	public String directOrderByProduct(
			@RequestParam("productId") Long productId,
			@RequestParam("quantity") int quantity,
			RedirectAttributes redirectAttributes
			) {
		
		redirectAttributes.addFlashAttribute("productId", productId);
		redirectAttributes.addFlashAttribute("quantity", quantity);
		
		return "redirect:/order";
	}
	
	
	@GetMapping("/order")
	public String showOrder(Model model,
			OrderDTO orderDTO,
			@ModelAttribute("productId") Long productId,
			@ModelAttribute("quantity") int quantity
			) {
		
		ProductDetailDto productInfo = this.productService.getProductById(productId);
		
		model.addAttribute("orderDTO", orderDTO);
		model.addAttribute("orderItem", new OrderItemDTO(productInfo, quantity));
		model.addAttribute("orderSummary", new OrderSummaryDTO());
		
		return "order";
	}
	
	@PostMapping("/order/process")
	public String orderProcess(OrderDTO orderDTO,
			OrderItemDTO orderItemDTO,
			OrderSummaryDTO summaryDTO,
			BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "/order";
		}
		
		this.orderService.process(orderDTO, orderItemDTO, summaryDTO);
		
		return "redirect:/";
	}
	
	
}
