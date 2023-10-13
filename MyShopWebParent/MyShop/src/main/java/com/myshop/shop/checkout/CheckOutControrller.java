package com.myshop.shop.checkout;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.myshop.common.entity.CartItem;
import com.myshop.common.entity.Customer;
import com.myshop.common.entity.ShippingRate;
import com.myshop.common.entity.order.Order;
import com.myshop.common.entity.order.PaymentMethod;
import com.myshop.shop.Utility;
import com.myshop.shop.customer.CustomerService;
import com.myshop.shop.order.OrderService;
import com.myshop.shop.setting.CurrencySettingBag;
import com.myshop.shop.setting.EmailSettingBag;
import com.myshop.shop.setting.SettingService;
import com.myshop.shop.shippingrate.ShippingRateNotFoundException;
import com.myshop.shop.shippingrate.ShippingRateService;
import com.myshop.shop.shopping.cart.CartItemService;

@Controller
public class CheckOutControrller {
	
	@Autowired 
	CheckOutService checkOutService;
	@Autowired 
	CustomerService customerService;
	@Autowired
	CartItemService cartItemService;
	@Autowired
	ShippingRateService rateService;
	@Autowired
	OrderService orderService;
	@Autowired
	SettingService settingService;
	@GetMapping("/checkout")
	public String viewCheckOut(Model model, HttpServletRequest request) throws ShippingRateNotFoundException  {
		
		Customer customer = getAuthenticatedCustomer(request);
		
		if(customer.isEmptyAddress()) return "redirect:/cart";
		List<CartItem> listCartItems = cartItemService.getCartByCustomer(customer);
		
		
		ShippingRate rate = rateService.getShippingRateByAddress(customer.getProvince(), customer.getDistrict());
		
		
		List<CartItem> cartItems =cartItemService.getCartByCustomer(customer);
		
		CheckOutInfo checkOutInfo = checkOutService.prepareCheckOut(cartItems, rate);
		
		model.addAttribute("shippingAddress", customer.getAddress());
		model.addAttribute("checkOutInfo", checkOutInfo);
		model.addAttribute("cartItems", cartItems);
		return "checkout/checkout";
	}
	
	private Customer getAuthenticatedCustomer( HttpServletRequest request)  {
		String email = Utility.getEmailCustomerFromAuthenticated(request);
		
		return customerService.getCustomerByEmail2(email);
	}
	
	@PostMapping("/place_order")
	public String placeOrder(HttpServletRequest request) throws ShippingRateNotFoundException, UnsupportedEncodingException, MessagingException {
		
		
		
		String paymentMethod = request.getParameter("paymentMethod");
		PaymentMethod paymentMethod2 = PaymentMethod.valueOf(paymentMethod);
		
		Customer customer = getAuthenticatedCustomer(request);
		
		if(customer.isEmptyAddress()) return "redirect:/cart";
		List<CartItem> listCartItems = cartItemService.getCartByCustomer(customer);
		
		
		ShippingRate rate = rateService.getShippingRateByAddress(customer.getProvince(), customer.getDistrict());
		
		
		List<CartItem> cartItems =cartItemService.getCartByCustomer(customer);
		
		CheckOutInfo checkOutInfo = checkOutService.prepareCheckOut(cartItems, rate);
		
		Order createdOrder = orderService.createOrder(customer, cartItems, paymentMethod2, checkOutInfo);
		
		cartItemService.deleteByCustomer(customer);
		
//		sendOrderConfirmationEmail(request, createdOrder);
		return "checkout/order_completed";
	}

	private void sendOrderConfirmationEmail(HttpServletRequest request, Order order) throws MessagingException, UnsupportedEncodingException {
		EmailSettingBag emailSettingBag = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettingBag);
		
		String toAddress = order.getCustomer().getEmail();
		String subject = emailSettingBag.getOrderConfirmationSubject();
		String content = emailSettingBag.getOrderConfirmationContent();
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		
		helper.setFrom(emailSettingBag.getFromAddress(), emailSettingBag.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		DateFormat dateFormatter =  new SimpleDateFormat("HH:mm:ss E, dd MMM yyyy");
		String orderTime = dateFormatter.format(order.getOrderTime());
		
		CurrencySettingBag currencySettings = settingService.getCurrencySettings();
		String totalAmount = Utility.formatCurrency(order.getTotal(), currencySettings);
		
		content = content.replace("[[name]]", order.getCustomer().getFullName());
		content = content.replace("[[orderId]]", String.valueOf(order.getId()));
		content = content.replace("[[orderTime]]", orderTime);
		content = content.replace("[[orderAddress]]", order.getCustomer().getAddress());
		content = content.replace("[[orderPriceTotal]]", totalAmount);
		content = content.replace("[[orderPaymentMethod]]", order.getPaymentMethod().toString());
		
		helper.setText(content, true);
		mailSender.send(message);		
	}
}
