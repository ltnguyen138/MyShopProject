package com.myshop.admin.setting;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myshop.common.entity.Currency;

@Service
public interface CurrencyService {
	public List<Currency> listAllCurrency() ;
	public Currency getCurrencyById(Integer id);
}
