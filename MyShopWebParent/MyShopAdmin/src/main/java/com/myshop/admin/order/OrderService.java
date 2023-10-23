package com.myshop.admin.order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.myshop.common.entity.Province;
import com.myshop.common.entity.order.Order;

@Service
public interface OrderService {
	public static final int ORDERS_PER_PAGE=6;
	public List<Order> listAll();
	public Order getOrderById(Integer id) throws OrderNotFoundException;
	public Page<Order> listByPage(int pageNum, String sortField,String sortDir,String keyword);
	public Order savedOrder(Order order);
	public void deleteById(Integer id) throws OrderNotFoundException;
	public List<Province> listAllProvice();
	public void save(Order orderInForm);
	public void updateStatus(Integer orderId, String status);
}
