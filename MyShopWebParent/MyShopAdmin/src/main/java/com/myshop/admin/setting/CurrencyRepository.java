package com.myshop.admin.setting;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.myshop.common.entity.Currency;

public interface CurrencyRepository extends CrudRepository<Currency, Integer>{
	List<Currency> findAllByOrderByNameAsc();
	Optional<Currency>  findById(Integer id);
}
