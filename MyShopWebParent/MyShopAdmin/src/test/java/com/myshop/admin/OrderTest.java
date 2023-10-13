package com.myshop.admin;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.myshop.admin.order.OrderDatailRepository;
import com.myshop.admin.order.OrderRepository;
import com.myshop.common.entity.Customer;
import com.myshop.common.entity.order.Order;
import com.myshop.common.entity.order.OrderDetail;
import com.myshop.common.entity.order.OrderStatus;
import com.myshop.common.entity.order.OrderTrack;
import com.myshop.common.entity.order.PaymentMethod;
import com.myshop.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderTest {
	
	@Autowired 
	OrderRepository orderRepository;
	@Autowired
	TestEntityManager entityManager;
	@Autowired
	OrderDatailRepository datailRepository;
	
	@Test
	public void testAdd() {
		Customer customer = entityManager.find(Customer.class, 2);
		Product product = entityManager.find(Product.class, 1);
		
		Order mainOrder = new Order();
		mainOrder.setOrderTime(new Date());
		mainOrder.setCustomer(customer);
		mainOrder.copyAndressFromCustomer();
		
		
		mainOrder.setShippingCost(10000D);
		mainOrder.setProductCost(product.getCost());
		mainOrder.setTax(0D);
		mainOrder.setSubtotal(product.getPrice());
		mainOrder.setTotal(product.getPrice() + 10000);
		
		mainOrder.setPaymentMethod(PaymentMethod.PAYPAL);
		mainOrder.setOrderStatus(OrderStatus.NEW);
		mainOrder.setDeliverDate(new Date());
		mainOrder.setDeliverDays(1);
		
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setProduct(product);
		orderDetail.setOrder(mainOrder);
		orderDetail.setProductCost(product.getCost());
		orderDetail.setShippingCost(10D);
		orderDetail.setQuantity(1);
		orderDetail.setSubtotal(product.getPrice());
		orderDetail.setUnitPrice(product.getPrice());
		
		mainOrder.getOrderDetails().add(orderDetail);
		
		Order savedOrder = orderRepository.save(mainOrder);
		
		assertThat(savedOrder.getId()).isGreaterThan(0);		
	}
	
	@Test
	public void testUpdateOrderTracks() {
		Integer orderId = 14;
		Order order = orderRepository.findById(orderId).get();
		
		OrderTrack newTrack = new OrderTrack();
		newTrack.setOrder(order);
		newTrack.setUpdatedTime(new Date());
		newTrack.setStatus(OrderStatus.NEW);
		newTrack.setNotes(OrderStatus.NEW.defaultDescription());

		OrderTrack processingTrack = new OrderTrack();
		processingTrack.setOrder(order);
		processingTrack.setUpdatedTime(new Date());
		processingTrack.setStatus(OrderStatus.PROCESSING);
		processingTrack.setNotes(OrderStatus.PROCESSING.defaultDescription());
		
		List<OrderTrack> orderTracks = order.getOrderTracks();
		orderTracks.add(newTrack);
		orderTracks.add(processingTrack);
		
		Order updatedOrder = orderRepository.save(order);
		
		assertThat(updatedOrder.getOrderTracks()).hasSizeGreaterThan(1);
	}
	
	@Test
	public void testFindByOrderTimeBetween() throws ParseException {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = dateFormatter.parse("2023-01-01");
		Date endTime = dateFormatter.parse("2023-10-06");
		
		List<Order> listOrders = orderRepository.findByOrderTimeBetween(startTime, endTime);
		
		assertThat(listOrders.size()).isGreaterThan(0);
		
		for (Order order : listOrders) {
			System.out.printf("%s | %s | %.2f | %.2f | %.2f \n", 
					order.getId(), order.getOrderTime(), order.getProductCost(), 
					order.getSubtotal(), order.getTotal());
		}
	}
	@Test
	public void testFindWithCategoryAndTimeBetween() throws ParseException {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = dateFormatter.parse("2023-01-01");
		Date endTime = dateFormatter.parse("2023-10-06");
		
		List<OrderDetail> listOrderDetails = datailRepository.findWithCategoryAndTimeBetween(startTime, endTime);
		
		assertThat(listOrderDetails.size()).isGreaterThan(0);
		
		for (OrderDetail detail : listOrderDetails) {
			System.out.printf("%-30s | %d | %10.2f| %10.2f | %10.2f \n", 
					detail.getProduct().getCategory().getName(),
					detail.getQuantity(), detail.getProductCost(),
					detail.getShippingCost(), detail.getSubtotal());
		}
	}
	@Test
	public void testFindWithProductNameAndTimeBetween() throws ParseException {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = dateFormatter.parse("2023-01-01");
		Date endTime = dateFormatter.parse("2023-10-06");
		
		List<OrderDetail> listOrderDetails = datailRepository.findWithProductNameAndTimeBetween(startTime, endTime);
		
		assertThat(listOrderDetails.size()).isGreaterThan(0);
		
		for (OrderDetail detail : listOrderDetails) {
			System.out.printf("%-30s | %d | %10.2f| %10.2f | %10.2f \n", 
					detail.getProduct().getName(),
					detail.getQuantity(), detail.getProductCost(),
					detail.getShippingCost(), detail.getSubtotal());
		}
	}
}
