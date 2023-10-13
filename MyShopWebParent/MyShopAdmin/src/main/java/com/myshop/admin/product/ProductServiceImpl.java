package com.myshop.admin.product;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.myshop.common.entity.product.Product;

@Component
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;
	
	@Override
	public Product getProductById(Integer id) throws ProductNotFoundException {

		return productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException("Sản phẩm có id "+id+ " không tồn tại"));
	}

	@Override
	public Product getProductByName(String name) throws ProductNotFoundException {

		return productRepository.findByName(name).orElseThrow(()-> new ProductNotFoundException("Sản phẩm có tên "+name+ " không tồn tại"));
	}
	
	@Override
	public Product getProductByAlias(String alias) {
		
		return productRepository.findByAlias(alias).orElse(null);
	}
	

	@Override
	public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword, Integer categoryId) {
		Sort sort=Sort.by(sortField);
		sort=sortDir.equals("asc")?sort.ascending():sort.descending();
		Pageable pageable=PageRequest.of(pageNum-1, PRODUCTS_PER_PAGE,sort);
		
		if (keyword!=null&&!keyword.isEmpty()) {
			if(categoryId>0) {
				String categoryAllParent ="-"+String.valueOf(categoryId)+"-";
				System.out.println(categoryAllParent);
				return productRepository.findCategoryWithKeyword(categoryId, categoryAllParent, keyword, pageable);
			}
			return productRepository.findKey(keyword, pageable);
		}

		if(categoryId>0) {
			String categoryAllParent ="-"+String.valueOf(categoryId)+"-";
			return productRepository.findAllInCategory(categoryId, categoryAllParent, pageable);
		}

		 return productRepository.findAll(pageable);
	}
	
	@Override
	public Page<Product> searchProduct(int pageNum, String sortField, String sortDir, String keyword){
		Sort sort=Sort.by(sortField);
		sort=sortDir.equals("asc")?sort.ascending():sort.descending();
		Pageable pageable=PageRequest.of(pageNum-1, PRODUCTS_PER_PAGE,sort);
		
		if (keyword!=null&&!keyword.isEmpty()) {

			return productRepository.searchProductsByName(keyword, pageable);
		}
		return productRepository.findAll(pageable);
	}

	@Override
	public Product saveProduct(Product product) {
		if (product.getId()==null) {
			product.setCreatedTime(new Date());
		}
		product.setAlias(product.getAlias().replace(" ", "_"));
		product.setUpdatedTime(new Date());
		return productRepository.save(product);
	}

	@Override
	public String checkUnique(Integer id, String name, String alias) {
		boolean isCreateNew = (id==null);
		Product getProductByName =productRepository.findByName(name).orElse(null);
		Product getProductByAlias=productRepository.findByAlias(alias).orElse(null);
		
		alias=alias.replace(" ", "_");
		if(isCreateNew) {
			if(getProductByName!=null) {
				return "DuplicateName";
			}else if (getProductByAlias!=null) {
				return "DuplicateAlias";
			}
		}else {
			if(getProductByName!=null&&getProductByName.getId()!=id) {
				return "DuplicateName";
			}else if (getProductByAlias!=null&&getProductByAlias(alias).getId()!=id) {
				return "DuplicateAlias";
			}
		}
		return "Ok";
	}

	@Override
	public void updateEnble(Integer id, boolean enabled) {
		productRepository.updateEnable(id, enabled);
		
	}

	@Override
	public void deleteProduct(Integer id) throws ProductNotFoundException {
		Product product =getProductById(id);
		productRepository.delete(product);
		
	}

	@Override
	public Product saveProductPrice(Product product) throws ProductNotFoundException {
	
		Product productInDb=getProductById(product.getId());
		productInDb.setCost(product.getCost());
		productInDb.setPrice(product.getPrice());
		productInDb.setDiscountPercent(product.getDiscountPercent());
		return productRepository.save(productInDb);
	}

	
	

}
