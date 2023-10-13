package com.myshop.shop.order;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.myshop.common.entity.CartItem;
import com.myshop.common.entity.Customer;
import com.myshop.common.entity.order.Order;
import com.myshop.common.entity.order.OrderDetail;
import com.myshop.common.entity.order.OrderStatus;
import com.myshop.common.entity.order.OrderTrack;
import com.myshop.common.entity.order.PaymentMethod;
import com.myshop.common.entity.product.Product;
import com.myshop.shop.checkout.CheckOutInfo;

@Component
public class OrderServiceImpl implements OrderService{

	@Autowired
	OrderRepository orderRepository;
	
	@Override
	public Order createOrder(Customer customer, List<CartItem> cartItems, PaymentMethod paymentMethod,
			CheckOutInfo checkOutInfo) {
		Order order = new Order();
		
		
		order.setProductCost(checkOutInfo.getProductCostTotal());
		order.setShippingCost(checkOutInfo.getShippingCostTotal());
		order.setSubtotal(checkOutInfo.getProductPriceTotal());
		order.setTotal(checkOutInfo.getPaymentTotal());
		order.setTax(0D);
		order.setPaymentMethod(paymentMethod);
		order.setDeliverDays(checkOutInfo.getDeliverDays());
		order.setDeliverDate(checkOutInfo.getDeliverDate());
		order.setOrderTime(new Date());
		order.setOrderStatus(OrderStatus.NEW);
		order.setCustomer(customer);
		
		order.copyAndressFromCustomer();
		
		Set<OrderDetail> orderDetails = order.getOrderDetails();
		for(CartItem item : cartItems) {
			OrderDetail detail = new OrderDetail();
			
			Product product = item.getProduct();
			
			detail.setOrder(order);
			detail.setProduct(product);
			detail.setProductCost(item.getQuantiny()*product.getCost());
			detail.setQuantity(item.getQuantiny());
			detail.setShippingCost(item.getShippingCost());
			detail.setSubtotal(item.getSubtotal());
			detail.setUnitPrice(product.getDiscountPrice());
			
			orderDetails.add(detail);
			
			
		}
		OrderTrack track = new OrderTrack();
		track.setOrder(order);
		track.setStatus(OrderStatus.NEW);
		track.setNotes(OrderStatus.NEW.defaultDescription());
		track.setUpdatedTime(new Date());
		
		order.getOrderTracks().add(track);
		return orderRepository.save(order);
	}

	@Override
	public Page<Order> listByPage(Customer customer,int pageNum, String sortField, String sortDir, String keyword) {
		
		Sort sort=Sort.by(sortField);
		
		
		if(sortDir==null) {
			sort=sort.ascending();
		
		}else if(sortDir.equals("asc")) {
			sort=sort.ascending();
		}else if(sortDir.equals("desc")){
			sort=sort.descending();
		}
		Pageable pageable=PageRequest.of(pageNum-1, ORDERS_PER_PAGE,sort);
		if(keyword != null) {
			return orderRepository.findKey(keyword,customer.getId() ,pageable);
		}else return orderRepository.findAll(customer.getId(), pageable);
		
	}

	@Override
	public Order getOrderById(Integer id, Customer customer) throws OrderNotFoundException {
	
		return orderRepository.findByIdAndCustomer(id, customer).orElseThrow(()-> new OrderNotFoundException("Không tìm thấy đơn hàng"));
	}

	@Override
	public void setOrderReturnRequested(OrderReturnRequest request, Customer customer) throws OrderNotFoundException {

		Order order = getOrderById(request.getOrderId(), customer);
		if (order.isReturnRequested()) return;
		
		OrderTrack track = new OrderTrack();
		track.setOrder(order);
		track.setUpdatedTime(new Date());
		track.setStatus(OrderStatus.RETURN_REQUESTED);
		
		String notes = "Lý do: " + request.getReason();
		if (!"".equals(request.getNote())) {
			notes += ". " + request.getNote();
		}
		track.setNotes(notes);
		
		order.getOrderTracks().add(track);
		order.setOrderStatus(OrderStatus.RETURN_REQUESTED);
		
		orderRepository.save(order);
	}
}
