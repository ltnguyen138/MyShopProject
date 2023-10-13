package com.myshop.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		exposeDir("user-photos", registry);
		
		exposeDir("../category-images", registry);
		
		exposeDir("../brand-logo", registry);
		
		exposeDir("../product-images", registry);
		
		exposeDir("../site-logo", registry);
	}
	
	private void exposeDir(String dirName, ResourceHandlerRegistry registry) {
		Path path=Paths.get(dirName);
		String absolutePath = path.toFile().getAbsolutePath();
		String logicalPath=dirName.replace("..", ""	);
		registry.addResourceHandler("/"+logicalPath+"/**").addResourceLocations("file:/"+absolutePath+"/");
	}
	
}
