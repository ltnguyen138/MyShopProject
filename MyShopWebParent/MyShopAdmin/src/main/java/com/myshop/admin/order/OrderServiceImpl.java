package com.myshop.admin.order;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.myshop.admin.setting.province.ProvinceRepository;
import com.myshop.common.entity.Province;
import com.myshop.common.entity.order.Order;
import com.myshop.common.entity.order.OrderStatus;
import com.myshop.common.entity.order.OrderTrack;

@Component
public class OrderServiceImpl implements OrderService{

	@Autowired
	OrderRepository orderRepository;
	@Autowired
	ProvinceRepository provinceRepository;
	
	public List<Province> listAllProvice() {
		
		return provinceRepository.findAllByOrderByNameAsc();
	}

	@Override
	public Order getOrderById(Integer id) throws OrderNotFoundException {
		return orderRepository.findById(id).orElseThrow(()-> new  OrderNotFoundException("Đơn hàng không tồn tại"));
	}

	@Override
	public Page<Order> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		Sort sort =null;
		if(sortField.equals("fullName")) {
			sort = Sort.by("firstName","lastName");
		}else if (sortField.equals("destination")) {
			sort = Sort.by("addressLine","district","province");
		}else {
			sort=Sort.by(sortField);
		}
		
		if(sortDir==null) {
			sort=sort.ascending();
		
		}else if(sortDir.equals("asc")) {
			sort=sort.ascending();
		}else if(sortDir.equals("desc")){
			sort=sort.descending();
		}
		Pageable pageable=PageRequest.of(pageNum-1, ORDERS_PER_PAGE,sort);
		if(keyword != null) {
			return orderRepository.findKey(keyword, pageable);
		}else return orderRepository.findAll(pageable);
		
	}

	@Override
	public Order savedOrder(Order order) {
//		boolean isUpdateOrder = order.getId()!=null;
		
		return orderRepository.save(order);
	}

	@Override
	public void deleteById(Integer id) throws OrderNotFoundException {
		Order order = getOrderById(id);
		orderRepository.delete(order);
		
	}

	@Override
	public void save(Order orderInForm) {
		Order orderInDB = orderRepository.findById(orderInForm.getId()).get();
		orderInForm.setOrderTime(orderInDB.getOrderTime());
		orderInForm.setCustomer(orderInDB.getCustomer());
		
		orderRepository.save(orderInForm);
		
	}

	public void updateStatus(Integer orderId, String status) {
		Order orderInDB = orderRepository.findById(orderId).orElse(null);
		OrderStatus statusToUpdate = OrderStatus.valueOf(status);
		System.out.print(orderId);
		if (!orderInDB.hasStatus(statusToUpdate)) {
			List<OrderTrack> orderTracks = orderInDB.getOrderTracks();
			
			OrderTrack track = new OrderTrack();
			track.setOrder(orderInDB);
			track.setStatus(statusToUpdate);
			track.setUpdatedTime(new Date());
			track.setNotes(statusToUpdate.defaultDescription());
			
			orderTracks.add(track);
			
			orderInDB.setOrderStatus(statusToUpdate);
			
			orderRepository.save(orderInDB);
		}
		
	}

	@Override
	public List<Order> listAll() {
		return (List<Order>) orderRepository.findAll();
	}
	
}
