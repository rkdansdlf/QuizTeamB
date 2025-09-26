package com.example.demo.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Orders {
	
	public static enum DeliveryType {NORMAL, EXPRESS, NURYEONG}
	
	public static enum PaymentMethod {CARD, BANK, MEMORY, TOSIM_MOOD}
	
	public static enum DeliverStatus {COMPLETED, SHIPPING}
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false)
	private String receiverName;
	
	@Column(nullable = false)
	private String receiverPhone;
	
	@Column(nullable = false)
	private String deliveryAddress;
	
	@Column
	private String specialRequest;

	private LocalDateTime createdAt;
	
	@Column
	private int totalAmount;
	
	@Enumerated(EnumType.STRING)
	private DeliveryType deliveryType;
	
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	
	@Enumerated(EnumType.STRING)
	private DeliverStatus status;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL) 
    private List<OrderItem> orderItems = new ArrayList<>();
	
	

	public Orders(String receiverName, String receiverPhone, String deliveryAddress, String specialRequest,
			DeliveryType deliveryType, PaymentMethod paymentMethod, int totalAmount) {
		this.receiverName = receiverName;
		this.receiverPhone = receiverPhone;
		this.deliveryAddress = deliveryAddress;
		this.specialRequest = specialRequest;
		
		this.deliveryType = deliveryType;
		this.paymentMethod = paymentMethod;

		this.status = DeliverStatus.SHIPPING;
		
		this.totalAmount = totalAmount;
		
		this.createdAt = LocalDateTime.now();
		
		
	}
	
	public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this); 
    }
	
	
}
