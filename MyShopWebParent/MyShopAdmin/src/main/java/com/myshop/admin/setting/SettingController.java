package com.myshop.admin.setting;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myshop.admin.FileUploadUtil;
import com.myshop.common.entity.Category;
import com.myshop.common.entity.Currency;
import com.myshop.common.entity.Setting;

@Controller
public class SettingController {
	
	@Autowired
	SettingService settingService;
	@Autowired
	CurrencyService currencyService;
	
	@GetMapping("/settings")
	public String listAll(Model model) {
		
		List<Setting> listAllSettings = settingService.listAllSetting();
		List<Currency> listAllCurrencies= currencyService.listAllCurrency();
		
		model.addAttribute("listAllCurrencies", listAllCurrencies);
		for(Setting setting : listAllSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}
		
		model.addAttribute("pageTitle","Cài đặt trang web");
		return "setting/setting";
	}
	
	@PostMapping("/settings/save_general")
	public String saveGeneralSetting(@RequestParam("images") MultipartFile multipartfile,HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws IOException {
		List<Setting> listSetting= settingService.getGeneralSetting();
		

		saveCurrencySymbol(request, listSetting);
		
		
		saveSiteLogo(multipartfile, listSetting);
		updateSettingValuesFromForm(request, listSetting);
		redirectAttributes.addFlashAttribute("message", "Lưu cài đặt chung thành công.");
		return "redirect:/settings";
	}

	private void saveSiteLogo(MultipartFile multipartfile, List<Setting> listSetting) throws IOException {
		if(!multipartfile.isEmpty()) {
			String filename=StringUtils.cleanPath(multipartfile.getOriginalFilename());
			String value="/site-logo/"+filename;
			settingService.updateSettingFromListSetting(listSetting, "SITE_LOGO", value);
			String uploadDir="../site-logo/";
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, filename, multipartfile);
		}
	}
	
	private void saveCurrencySymbol(HttpServletRequest request, List<Setting> listSetting) {
		Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
		Currency currency = currencyService.getCurrencyById(currencyId);
		
		if (currency!=null) {
			settingService.updateSettingFromListSetting(listSetting, "CURRENCY_SYMBOL", currency.getSymbol());
		}
	}
	
	private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
		for (Setting setting : listSettings) {
			String value = request.getParameter(setting.getKey());
			if (value != null) {
				setting.setValue(value);
			}
		}
		
		settingService.saveAll(listSettings);
	}
	
	@PostMapping("/settings/save_mail_server")
	public String saveMailServerSetting(HttpServletRequest request,RedirectAttributes redirectAttributes) {
		List<Setting> listMailServers = settingService.getMailServerSetting();
		updateSettingValuesFromForm(request, listMailServers);
		
		redirectAttributes.addFlashAttribute("message", "Lưu cài đặt mail server thành công.");
		return "redirect:/settings#mailServer";
	}
	
	@PostMapping("/settings/save_mail_templates")
	public String saveMailTemplatesSetting(HttpServletRequest request,RedirectAttributes redirectAttributes) {
		List<Setting> listMailServers = settingService.getMailTemplatesSetting();
		updateSettingValuesFromForm(request, listMailServers);
		
		redirectAttributes.addFlashAttribute("message", "Lưu cài đặt mẫu mail thành công.");
		return "redirect:/settings#mailTemple";
	}
}
