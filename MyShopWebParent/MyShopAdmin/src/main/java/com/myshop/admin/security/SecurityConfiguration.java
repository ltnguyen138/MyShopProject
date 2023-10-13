package com.myshop.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	 @Bean
	    public UserDetailsService userDetailsService() {
	        return new MyShopUserDetailService();
	    }
	@Bean
	public PasswordEncoder PasswordEncoder() {	
		return new BCryptPasswordEncoder();
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(requests -> requests
    			.antMatchers("/states/list_by_country/**").hasAnyAuthority("Admin", "Salesperson")
    			.antMatchers("/users/**", "/settings/**", "/countries/**", "/states/**").hasAuthority("Admin")
    			.antMatchers("/categories/**", "/brands/**").hasAnyAuthority("Admin", "Editor")
    			
    			.antMatchers("/products/new", "/products/delete/**").hasAnyAuthority("Admin", "Editor")
    			
    			.antMatchers("/products/edit/**", "/products/save", "/products/check_unique")
    				.hasAnyAuthority("Admin", "Editor", "Salesperson")
    				
    			.antMatchers("/products", "/products/", "/products/detail/**", "/products/page/**")
    				.hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
    				
    			.antMatchers("/products/**").hasAnyAuthority("Admin", "Editor")
    			
    			.antMatchers("/orders", "/orders/", "/orders/page/**", "/orders/detail/**").hasAnyAuthority("Admin", "Salesperson", "Shipper")
    			
    			.antMatchers("/products/detail/**", "/customers/detail/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Assistant")

    			.antMatchers("/customers/**", "/orders/**", "/get_shipping_cost", "/reports/**").hasAnyAuthority("Admin", "Salesperson")
    			
    			
    			
    			.antMatchers("/reviews/**").hasAnyAuthority("Admin", "Assistant")

                .anyRequest().authenticated())
        
                .formLogin(login -> login
                        .loginPage("/login")
                        .usernameParameter("email")
                        .permitAll()).logout(logout -> logout.permitAll())
                .rememberMe(remember->remember.key("LeTrangNguyen1308"))
			;
               
			
       	
        http.headers(headers -> headers.frameOptions().sameOrigin());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
	}

	
	 @Bean
	 public DaoAuthenticationProvider authenticationProvider() {
	     DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	      
	     authProvider.setUserDetailsService(userDetailsService());
	     authProvider.setPasswordEncoder(PasswordEncoder());
	  
	     return authProvider;
	 }
}
