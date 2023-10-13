package com.myshop.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.myshop.common.entity.Setting;
import com.myshop.common.entity.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, String> {
	List<Setting> findByCategory(SettingCategory category);
}
