package com.myshop.shop.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.myshop.common.entity.product.Product;

@Component
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;
	
	@Override
	public Page<Product> listByCategory(int pageNum, Integer categoryId) {
		String categoryAllChild="-"+String.valueOf(categoryId)+"-";
		Pageable pageable= PageRequest.of(pageNum-1,PRODUCTS_PER_PAGE );
		
		
		return productRepository.listByCategory(categoryId, categoryAllChild, pageable);
	}

	@Override
	public Product getProductByAlias(String alias) throws ProductNotFoundException {
		
		return productRepository.findByAlias(alias).orElseThrow(()-> new ProductNotFoundException("Không tìm thấy sản phẩm"+alias));
	}

	@Override
	public Page<Product> searchProduct(int pageNum, String keyword) {
		Pageable pageable= PageRequest.of(pageNum-1,PRODUCTS_PER_PAGE );
		
		return productRepository.findKey(keyword, pageable);
	}

}
