package com.myshop.shop.product;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.myshop.common.entity.product.Product;

@Service
public interface ProductService {
	public static final int PRODUCTS_PER_PAGE=12;
	public Page<Product> listByCategory(int pageNum, Integer categoryId);
	public Product getProductByAlias(String alias) throws ProductNotFoundException;
	public Page<Product> searchProduct(int pageNum, String keyword);
}
