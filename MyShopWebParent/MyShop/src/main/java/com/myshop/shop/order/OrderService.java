package com.myshop.shop.order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.myshop.common.entity.CartItem;
import com.myshop.common.entity.Customer;
import com.myshop.common.entity.order.Order;
import com.myshop.common.entity.order.PaymentMethod;
import com.myshop.shop.checkout.CheckOutInfo;

@Service
public interface OrderService {
	public static final int ORDERS_PER_PAGE=6;
	public Order createOrder(Customer customer, List<CartItem> cartItems, PaymentMethod paymentMethod, CheckOutInfo checkOutInfo);
	public Page<Order> listByPage( Customer customer,int pageNum, String sortField,String sortDir,String keyword);
	public Order getOrderById(Integer id, Customer customer) throws OrderNotFoundException;
	public void setOrderReturnRequested(OrderReturnRequest request, Customer customer) throws OrderNotFoundException;

}
