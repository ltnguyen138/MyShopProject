package com.myshop.admin.setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.myshop.common.entity.Currency;

@Component
public class CurrencyServiceImpl implements CurrencyService {

	@Autowired
	CurrencyRepository currencyRepository;
	@Override
	public List<Currency> listAllCurrency() {

		return currencyRepository.findAllByOrderByNameAsc();
	}
	@Override
	public Currency getCurrencyById(Integer id) {
		return currencyRepository.findById(id).orElse(null);
	}

}
