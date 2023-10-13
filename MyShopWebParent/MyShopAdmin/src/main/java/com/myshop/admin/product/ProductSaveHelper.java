package com.myshop.admin.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.myshop.admin.FileUploadUtil;
import com.myshop.common.entity.product.Product;
import com.myshop.common.entity.product.ProductImage;

public class ProductSaveHelper {
	public static void deleteExtraImagesWeredRemovedOnForm(Product product) {
		
		String extraDir="../product-images/"+product.getId()+"/extras";
		Path dirPath=Paths.get(extraDir);
		try {
			Files.list(dirPath).forEach(file->{
				String fileName=file.toFile().getName();
				if (!product.containsImageName(fileName)) {
					try {
						Files.delete(file);
					}catch (IOException e) {
						// TODO: handle exception
					}
				}
			});
			
		} catch (IOException ex) {
			// TODO: handle exception
		}
	}

	public static void setExistExtraImages(Product product, String[] extraImagesId, String[] extraImagesName) {
		if(extraImagesId==null||extraImagesId.length==0)return;
		
		Set<ProductImage>productImages=new HashSet<>();
		for(int count=0;count<extraImagesId.length;count++) {
			Integer id=Integer.parseInt(extraImagesId[count]);
			String name=extraImagesName[count];
			productImages.add(new ProductImage(id, name, product));
		}
		product.setImages(productImages);
	}

	public static void setProductDetails(Product product, String[] detailId, String[] detailNames, String[] detailValues) {
		if(detailNames==null||detailNames.length==0)return;
		for(int i=0;i<detailNames.length;i++) {
			String name=detailNames[i];
			String value=detailValues[i];
			Integer id=Integer.parseInt(detailId[i]) ;
			if(id!=0) {
				product.addDetails(id,name, value);
			}else {
				if(!name.isEmpty()&&!value.isEmpty()) { 
					product.addDetails(name, value);
				}
			}
			
		}
		
		
	}

	public static void saveUploadImages(Product saveProduct,MultipartFile mainImageMultipart,
			MultipartFile[] extraImageMultipart) throws IOException {
		
		if(!mainImageMultipart.isEmpty()) {
			String fileName=StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			String uploadDir="../product-images/"+saveProduct.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);			
		}
		
		if(extraImageMultipart.length>0) {
			String uploadDir="../product-images/"+saveProduct.getId()+"/extras";
			for(MultipartFile multipartFile :extraImageMultipart) {
				if(!multipartFile.isEmpty()) {
					String fileName=StringUtils.cleanPath(multipartFile.getOriginalFilename());
					FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);	
				}
			}
		}
	}
	
	public static void setNewExtraImageName(Product product,MultipartFile[] extraImageMultipart) {
		if(extraImageMultipart.length>0) {
			for(MultipartFile multipartFile :extraImageMultipart) {
				if(!multipartFile.isEmpty()) {
					String fileName=StringUtils.cleanPath(multipartFile.getOriginalFilename());
					if(!product.containsImageName(fileName)) {
						product.addExtraImage(fileName);
					}
					
				}
			}
		}
	}
	
	public static void setMainImageName(Product product,MultipartFile mainImageMultipart ) {
		
		if(!mainImageMultipart.isEmpty()) {
			String fileName=StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			product.setMainImage(fileName);
		}
	}
}
