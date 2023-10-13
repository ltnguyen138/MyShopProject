package com.myshop.admin.setting;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myshop.common.entity.Setting;

@Service
public interface SettingService {

	public List<Setting> listAllSetting();
	
	public void saveAll(Iterable<Setting> settings);
	public List<Setting> getGeneralSetting();
	public Setting getSettingFromListSetting(List<Setting>listSettings ,String key);
	public void updateSettingFromListSetting(List<Setting> listSettings ,String key ,String value);
	public List<Setting> getMailServerSetting();
	public List<Setting> getMailTemplatesSetting();
	public List<Setting> getMailServerAndTemplatesSetting();

	Setting getSettingFromListSetting2(List<Setting> listSettings, String key);
}
