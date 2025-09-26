package com.example.demo.order;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.order.Orders.DeliveryType;
import com.example.demo.order.Orders.PaymentMethod;
import com.example.demo.product.Product;
import com.example.demo.product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
	
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	
	public void process(
			OrderDTO orderDTO, 
			OrderItemDTO orderItemDTO, 
			OrderSummaryDTO summaryDTO
			) {
		
		DeliveryType deliveryType;
		PaymentMethod paymentMethod;
		
		if(orderDTO.getDeliveryType().equals("NORMAL")) {
			deliveryType = DeliveryType.NORMAL;
		} else if(orderDTO.getDeliveryType().equals("EXPRESS")) {
			deliveryType = DeliveryType.EXPRESS;
		} else if(orderDTO.getDeliveryType().equals("NURYEONG")) {
			deliveryType = DeliveryType.NURYEONG;
		} else {
			throw new IllegalArgumentException("유효하지 않은 배송 방법입니다.");
		}
		
		if(orderDTO.getPaymentMethod().equals("CARD")) {
			paymentMethod = PaymentMethod.CARD;
		} else if(orderDTO.getPaymentMethod().equals("BANK")) {
			paymentMethod = PaymentMethod.BANK;
		} else if(orderDTO.getPaymentMethod().equals("MEMORY")) {
			paymentMethod = PaymentMethod.MEMORY;
		} else if (orderDTO.getPaymentMethod().equals("TOSIM_MOOD")) {
			paymentMethod = PaymentMethod.TOSIM_MOOD;
		} else {
			throw new IllegalArgumentException("유효하지 않은 결제 방법입니다.");
		}
		
		Orders order = new Orders(
				orderDTO.getReceiverName(),
				orderDTO.getReceiverPhone(),
				orderDTO.getDeliveryAddress(),
				orderDTO.getSpecialRequest(),
				deliveryType,
				paymentMethod,
				summaryDTO.getTotalAmount()
				);
		
		Product product 
			= this.productRepository.findById(orderItemDTO.getProduct().getId()).get();
		
		OrderItem orderItem = new OrderItem(
				order,
				product,
				orderItemDTO.getQuantity()
				);
		
		if(orderDTO.isPrivacyAgreed()==true && orderDTO.isTermsAgreed()==true) {
			this.orderRepository.save(order);
		} else {
			throw new IllegalArgumentException("약관에 동의하지 않으면 주문할 수 없습니다.");
		}
		
	}

}
