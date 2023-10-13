package com.myshop.admin.product;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.myshop.common.entity.product.Product;

@Service
public interface ProductService {
	
	public static final int PRODUCTS_PER_PAGE=6;
	
	public Product getProductById(Integer id) throws ProductNotFoundException;
	public Product getProductByName(String name) throws ProductNotFoundException;
	public Product getProductByAlias(String alias);
	public Page<Product> listByPage(int pageNum,String sortField, String sortDir,String keyword, Integer categoryId);
	public Product saveProduct(Product product);
	public String checkUnique(Integer id, String name, String alias);
	public void updateEnble(Integer id, boolean enabled);
	public void deleteProduct(Integer id) throws ProductNotFoundException;
	public Product saveProductPrice(Product product) throws ProductNotFoundException;
	public Page<Product> searchProduct(int pageNum, String sortField, String sortDir, String keyword);
}
