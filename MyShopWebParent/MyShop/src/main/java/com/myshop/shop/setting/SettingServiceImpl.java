package com.myshop.shop.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.myshop.common.entity.Setting;
import com.myshop.common.entity.SettingCategory;
@Component
public class SettingServiceImpl implements SettingService {

	@Autowired
	SettingRepository settingRepository;
	
	@Override
	public List<Setting> listAllSetting() {
		
		return  (List<Setting>) settingRepository.findAll();
	}

	@Override
	public Setting getSettingFromListSetting(List<Setting>listSettings ,String key) {
		int index= listSettings.indexOf(new Setting(key));
		if(index>=0) {
			return listSettings.get(index);
		}
		return null;
	}	
	
	@Override
	public List<Setting> getGeneralSetting() {
		List<Setting> settings = new ArrayList<>();
		
		List<Setting> generalSettings = settingRepository.findByCategory(SettingCategory.GENERAL);
		List<Setting> currencySettings = settingRepository.findByCategory(SettingCategory.CURRENCY);
		
		settings.addAll(generalSettings);
		settings.addAll(currencySettings);
		
		return settings;
	}

	@Override
	public List<Setting> getMailServerSetting() {
		List<Setting> settings = new ArrayList<>();
		
		List<Setting> mailServerSettings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
		
		
		settings.addAll(mailServerSettings);
		
		
		return settings;
	}


	@Override
	public List<Setting> getMailTemplatesSetting() {
		List<Setting> settings = new ArrayList<>();
		
		List<Setting> mailServerSettings = settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
		
		
		settings.addAll(mailServerSettings);
		
		
		return settings;
	}


	@Override
	public List<Setting> getMailServerAndTemplatesSetting() {
		List<Setting> settings = new ArrayList<>();

		settings.addAll(getMailServerSetting());
		settings.addAll(getMailTemplatesSetting());
		
		return settings;
	}

	@Override
	public EmailSettingBag getEmailSettings() {
		
		List<Setting> settings=getMailServerAndTemplatesSetting();
		return new EmailSettingBag(settings);
	}

	@Override
	public CurrencySettingBag getCurrencySettings() {
		List<Setting> settings=getGeneralSetting();
		return new CurrencySettingBag(settings);
	}
}
