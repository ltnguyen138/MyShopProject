package com.myshop.shop.customer.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.myshop.common.entity.Customer;
import com.myshop.shop.Utility;
import com.myshop.shop.customer.CustomerNotFoundException;
import com.myshop.shop.customer.CustomerService;
import com.myshop.shop.setting.EmailSettingBag;
import com.myshop.shop.setting.SettingService;

@Controller
public class ForgotPasswordController {
	@Autowired 
	private CustomerService customerService;
	@Autowired
	private SettingService settingService;
	
	@GetMapping("/forgot_password")
	public String viewForgotPassword() {
		return "customer/forgot_password_form";
	}
	
	@PostMapping("/forgot_password")
	public String processForgotPassword(HttpServletRequest request , Model model) {
		String email = request.getParameter("email");
		try {
			String token = customerService.updateResetPasswordToken(email);
			String link = Utility.getSiteUrl(request)+"/reset_password?token="+token;
			
			sendMailResetPassword(link, email);
			
			model.addAttribute("message", "Email cấp lại mật khẩu đã được gửi, vui lòng check email để tiếp tục");
			
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("messageError", "Email xác nhận đổi mật khẩu gửi không thành công, vui lòng thực hiện lại");
			
			
		} catch (CustomerNotFoundException e) {
			model.addAttribute("messageError", e.getMessage());
			
		}
		
		return "customer/forgot_password_form";
	}
	
	private void sendMailResetPassword(String link, String email) throws MessagingException, UnsupportedEncodingException {
		EmailSettingBag emailSettingBag = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettingBag);
		
		String toAddress= email;
		String subject = "Đường dẫn đặt lại mật khẩu";
		String content = "<p>Xin chào, </p>"
				+"<p>Bạn đã yêu cầu đặt lại mật khẩu, </p>"
				+"<p>Vui lòng nhấp vào đường link bên dưới để có thể thay đổi mật khẩu </p>"
				+"<p><a href=\""+link+"\"> Đổi mật khẩu của bạn</a> </p>";
				
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		
		helper.setFrom(emailSettingBag.getFromAddress(), emailSettingBag.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		helper.setText(content, true);
		
		mailSender.send(message);
	}
	
	@GetMapping("/reset_password")
	public String showResetPasswordForm(Model model, @Param("token") String token) {
		if(token==null||token.isEmpty()) {
			model.addAttribute("messageError", "Cấp lại mật khẩu không tành công");
			return "customer/message";
		}
		try {
			Customer customer = customerService.getCustomerByResetPasswordToken(token);
			model.addAttribute("token", customer.getResetPasswordToken());
			return "customer/reset_password_form";
		} catch (CustomerNotFoundException e) {
			model.addAttribute("messageError", e.getMessage());
			return "customer/message";
		}
		
	}
	
	@PostMapping("/reset_password")
	public String processResetPasswordForm(HttpServletRequest request , Model model) {
		String token = request.getParameter("token");
		String password= request.getParameter("password");
		
		try {
			customerService.resetPassword(token, password);
			model.addAttribute("message","Cấp lại mật khẩu thành công");
			return "customer/message";
		} catch (CustomerNotFoundException e) {
			model.addAttribute("messageError", e.getMessage());
			return "customer/message";
		}
		
	}
}
