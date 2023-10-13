package com.myshop.shop.customer.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myshop.common.entity.Customer;
import com.myshop.common.entity.Province;
import com.myshop.shop.Utility;
import com.myshop.shop.customer.CustomerService;
import com.myshop.shop.security.CustomerUserDetail;
import com.myshop.shop.security.oauth.customerOauth2User;
import com.myshop.shop.setting.EmailSettingBag;
import com.myshop.shop.setting.SettingService;

@Controller
public class CustomerController {
	@Autowired
	CustomerService customerService;
	@Autowired
	SettingService settingService;
	
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		List<Province> listProvincies = customerService.listAllProvice();
		Customer customer = new Customer();
		customer.setId(null);
		model.addAttribute("listProvincies", listProvincies);
		model.addAttribute("customer", customer);
		model.addAttribute("pageTitle", "Đăng ký tài khoản");
		return "register/register_form";
	}
	
	@PostMapping("/create_customer")
	public String createCustomer(Customer customer, Model model, HttpServletRequest request	) throws UnsupportedEncodingException, MessagingException {
		customerService.registerCustomer(customer);
		sendVerificationEmail(request, customer);
		model.addAttribute("pageTitle", "Đăng kí thành công");
		return "register/register_success";
	}
	
	private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettingBag = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettingBag);
		
		String toAddress= customer.getEmail();
		String subject = emailSettingBag.getCustomerVerifySubject()	;
		String content = emailSettingBag.getCustomerVerifyContent()	;
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		
		helper.setFrom(emailSettingBag.getFromAddress(), emailSettingBag.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		content = content.replace("[[name]]", customer.getFirstName());
		String verifyUrl= Utility.getSiteUrl(request)+"/verify?code="+customer.getVerificationCode();
		content = content.replace("[[URL]]", verifyUrl);
		helper.setText(content, true);
		
		mailSender.send(message);
		System.out.println("addd:"+content	);
		System.out.println("addd:"+helper.getEncoding()	);
		System.out.println("addd:"+toAddress	);
		System.out.println("url:"+verifyUrl	);
	}
	
	@GetMapping("/verify")
	public String verifyCustomerAccount(@Param("code") String code, Model model) {
		boolean verifyed=customerService.verifyCustomerAccount(code);
		return "register/"+(verifyed ? "verify_success" : "verify_fail");
	}
	
	@GetMapping("/account_details")
	public String viewAccountDetails(Model model, HttpServletRequest request) {
		
		String email = Utility.getEmailCustomerFromAuthenticated(request);
		System.out.print("----------------------"+email);
		Customer customer = customerService.getCustomerByEmail2(email);
		
		List<Province> listProvincies = customerService.listAllProvice();
		
		model.addAttribute("listProvincies", listProvincies);
		model.addAttribute("customer", customer);
		return "customer/account_form";
	}
	
	
	@PostMapping("/update_account")
	public String updateAccount(Model model, Customer customer, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		
		customerService.updateAccount(customer);
		updateNameForAuthenticated(request, customer);
		
		String redirectOption = request.getParameter("redirect");
		String redirectUrl = "redirect:/account_details";
		
		if("cart".equals(redirectOption)) {
			redirectUrl = "redirect:/cart";
		}
		redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin thành công.");
		
		return redirectUrl;
	}

	private void updateNameForAuthenticated(HttpServletRequest request, Customer customer) {
		Object principal = request.getUserPrincipal();
		String fullName= customer.getLastName()+" "+customer.getFirstName();
		if(principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken) {
			CustomerUserDetail userDetail = getCustomerDetail(principal);
			Customer customerAuthentication = userDetail.getCustomer();
			customerAuthentication.setLastName(customer.getLastName());
			customerAuthentication.setFirstName(customer.getFirstName());
			
		}else if (principal instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
			customerOauth2User oauth2User = (customerOauth2User) oAuth2AuthenticationToken.getPrincipal();
			oauth2User.setFullName(fullName);
		}
		
	}
	
	private CustomerUserDetail getCustomerDetail(Object principal) {
		CustomerUserDetail customerUserDetail=null;
		if(principal instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
			customerUserDetail = (CustomerUserDetail) token.getPrincipal();
		}else if (principal instanceof RememberMeAuthenticationToken) {
			RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal ;
			customerUserDetail = (CustomerUserDetail) token.getPrincipal();
		}
		return customerUserDetail;
	}
	
}
