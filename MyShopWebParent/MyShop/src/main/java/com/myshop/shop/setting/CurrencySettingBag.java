package com.myshop.shop.setting;

import java.util.List;

import com.myshop.common.entity.Setting;
import com.myshop.common.entity.SettingBag;

public class CurrencySettingBag extends SettingBag {

	public CurrencySettingBag(List<Setting> listSettings) {
		super(listSettings);
		// TODO Auto-generated constructor stub
	}

	public String getSymbol() {
		return super.getValue("CURRENCY_SYMBOL");
	}
	
	public String getSymbolPosition() {
		return super.getValue("CURRENCY_SYMBOL_POSITION");
	}
	
	public String getDecimalPointType() {
		return super.getValue("DECIMAL_POINT_TYPE");
	}

	public String getThousandPointType() {
		return super.getValue("THOUSANDS_POINT_TYPE");
	}	
	
	public int getDecimalDigits() {
		return Integer.parseInt(super.getValue("DECIMAL_DIGITS"));
	}
	
}
