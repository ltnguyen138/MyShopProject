package com.myshop.admin.order.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myshop.admin.customers.CustomerNotFoundException;
import com.myshop.admin.order.OrderCsvExporter;
import com.myshop.admin.order.OrderNotFoundException;
import com.myshop.admin.order.OrderService;
import com.myshop.admin.user.export.UserCsvExporter;
import com.myshop.common.entity.Province;
import com.myshop.common.entity.User;
import com.myshop.common.entity.order.Order;
import com.myshop.common.entity.order.OrderDetail;
import com.myshop.common.entity.order.OrderStatus;
import com.myshop.common.entity.order.OrderTrack;
import com.myshop.common.entity.product.Product;

@Controller
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@GetMapping("/orders")
	public String listFirst(Model model) {
		
		return listByPage(1, model,"firstName","asc","");
	}
	
	@GetMapping("/orders/page/{pageNum}")
	public String listByPage(@PathVariable("pageNum") int pageNum,Model model,@Param("sortField")String sortField,
			@Param("sortDir")String sortDir,@Param("keyword")String keyword ) {
		
		Page<Order> pageOrder = orderService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Order> listAllOrders = pageOrder.getContent();
		
		long startCount=(pageNum-1)*orderService.ORDERS_PER_PAGE+1;
		long endCount=startCount+orderService.ORDERS_PER_PAGE-1;
		if(endCount>pageOrder.getTotalElements())endCount=pageOrder.getTotalElements();
		String reverseSortDir=sortDir.equals("asc")?"desc" : "asc";
		model.addAttribute("totalPages", pageOrder.getTotalPages());
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageOrder.getTotalElements());
		model.addAttribute("listAllOrders", listAllOrders);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "orders/orders";
	}
	
	@GetMapping("/shipping")
	public String slistFirst(Model model) {
		
		return slistByPage(1, model,"id","asc","");
	}
	
	@GetMapping("/shipping/page/{pageNum}")
	public String slistByPage(@PathVariable("pageNum") int pageNum,Model model,@Param("sortField")String sortField,
			@Param("sortDir")String sortDir,@Param("keyword")String keyword ) {
		
		Page<Order> pageOrder = orderService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Order> listAllOrders = pageOrder.getContent();
		
		long startCount=(pageNum-1)*orderService.ORDERS_PER_PAGE+1;
		long endCount=startCount+orderService.ORDERS_PER_PAGE-1;
		if(endCount>pageOrder.getTotalElements())endCount=pageOrder.getTotalElements();
		String reverseSortDir=sortDir.equals("asc")?"desc" : "asc";
		model.addAttribute("totalPages", pageOrder.getTotalPages());
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageOrder.getTotalElements());
		model.addAttribute("listAllOrders", listAllOrders);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "orders/orders_shipper";
	}
	@GetMapping("/orders/detail/{id}")
	public String detailOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes ) {
		
		try {
			Order order = orderService.getOrderById(id);
			model.addAttribute("order", order);
			
			return "orders/order_detail_modal";
		} catch (OrderNotFoundException e) {			
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/orders";
		}
	}
	
	@GetMapping("/orders/delete/{id}")
	public String deleteCustomer(@PathVariable(name="id") int id , RedirectAttributes redirectattributes, Model model)  {
					
		try {
			orderService.deleteById(id);
			redirectattributes.addFlashAttribute("message", "Xóa khách hàng thành công.");
		} catch (OrderNotFoundException e) {
			redirectattributes.addFlashAttribute("errorMessage",e.getMessage());
			
		}
		return "redirect:/orders";
	}
	@GetMapping("/orders/edit/{id}")
	public String editOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes ra,
			HttpServletRequest request) {
		try {
			Order order = orderService.getOrderById(id);
			
			List<Province> listProvinces = orderService.listAllProvice();
			
			model.addAttribute("pageTitle", "Cập nhật đơn hàng (ID: " + id + ")");
			model.addAttribute("order", order);
			model.addAttribute("listProvincies", listProvinces);
			
			return "orders/order_form";
			
		} catch (OrderNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return "redirect:/orders";
		}
		
	}	
	
	@PostMapping("/order/save")
	public String saveUpdateOrder(Order order, HttpServletRequest request, RedirectAttributes redirectattributes) {
		
		String provinceName = request.getParameter("provinceName");
		order.setProvince(provinceName);
		
		updateProductDetails(order, request);
		updateOrderTracks(order, request);

		orderService.save(order);	
		
		return "redirect:/orders";
	}
	
	private void updateOrderTracks(Order order, HttpServletRequest request) {
		String[] trackIds = request.getParameterValues("trackId");
		String[] trackStatuses = request.getParameterValues("trackStatus");
		String[] trackDates = request.getParameterValues("trackDate");
		String[] trackNotes = request.getParameterValues("trackNotes");
		
		List<OrderTrack> orderTracks = order.getOrderTracks();
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		
		for (int i = 0; i < trackIds.length; i++) {
			OrderTrack trackRecord = new OrderTrack();
			
			Integer trackId = Integer.parseInt(trackIds[i]);
			if (trackId > 0) {
				trackRecord.setId(trackId);
			}
			
			trackRecord.setOrder(order);
			trackRecord.setStatus(OrderStatus.valueOf(trackStatuses[i]));
			trackRecord.setNotes(trackNotes[i]);
			
			try {
				trackRecord.setUpdatedTime(dateFormatter.parse(trackDates[i]));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			orderTracks.add(trackRecord);
		}
	}

	private void updateProductDetails(Order order, HttpServletRequest request) {
		String[] detailIds = request.getParameterValues("detailId");
		String[] productIds = request.getParameterValues("productId");
		String[] productPrices = request.getParameterValues("productPrice");
		String[] productDetailCosts = request.getParameterValues("productDetailCost");
		String[] quantities = request.getParameterValues("quantity");
		String[] productSubtotals = request.getParameterValues("productSubtotal");
		String[] productShipCosts = request.getParameterValues("productShipCost");
		
		Set<OrderDetail> orderDetails = order.getOrderDetails();
		
		for (int i = 0; i < detailIds.length; i++) {
			System.out.println("Detail ID: " + detailIds[i]);
			System.out.println("\t Prodouct ID: " + productIds[i]);
			System.out.println("\t Cost: " + productDetailCosts[i]);
			System.out.println("\t Quantity: " + quantities[i]);
			System.out.println("\t Subtotal: " + productSubtotals[i]);
			System.out.println("\t Ship cost: " + productShipCosts[i]);
			
			OrderDetail orderDetail = new OrderDetail();
			Integer detailId = Integer.parseInt(detailIds[i]);
			if (detailId > 0) {
				orderDetail.setId(detailId);
			}
			
			orderDetail.setOrder(order);
			orderDetail.setProduct(new Product(Integer.parseInt(productIds[i])));
			orderDetail.setProductCost(Double.parseDouble(productDetailCosts[i]));
			orderDetail.setSubtotal(Double.parseDouble(productSubtotals[i]));
			orderDetail.setShippingCost(Double.parseDouble(productShipCosts[i]));
			orderDetail.setQuantity(Integer.parseInt(quantities[i]));
			orderDetail.setUnitPrice(Double.parseDouble(productPrices[i]));
			
			orderDetails.add(orderDetail);
			
		}
		
	}
	@GetMapping("/orders/export/csv")
	public void  exportCSV(HttpServletResponse respone) throws IOException {
		List<Order> listOrder=orderService.listAll();
		OrderCsvExporter orderCsvExporter= new OrderCsvExporter();
		orderCsvExporter.export(listOrder, respone);
	}
}
