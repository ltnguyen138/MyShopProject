package com.myshop.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication()
@EntityScan({"com.myshop.common.entity","com.myshop.admin.user"})
public class MyShopAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyShopAdminApplication.class, args);
	}
	
}
