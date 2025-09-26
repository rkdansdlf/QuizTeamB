// OrderItem.java
package com.example.demo.order;

import com.example.demo.product.Product; // Product 엔티티를 import
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;


    // 생성자
    public OrderItem(Orders order, Product product, int quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

	public void setOrder(Orders orders) {
		this.order = orders;
	}
}