package com.myshop.shop.setting;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myshop.common.entity.Setting;

@Service
public interface SettingService {

	public List<Setting> listAllSetting();
	public CurrencySettingBag getCurrencySettings();
	
	public List<Setting> getGeneralSetting();
	
	public List<Setting> getMailServerSetting();
	public List<Setting> getMailTemplatesSetting();
	public List<Setting> getMailServerAndTemplatesSetting();
	public Setting getSettingFromListSetting(List<Setting>listSettings ,String key);
	public EmailSettingBag getEmailSettings();
}
