package com.myshop.shop.security;

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
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.myshop.shop.security.oauth.OAuth2LoginSuccessHandler;
import com.myshop.shop.security.oauth.customerOauth2UserService;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Bean
	public SavedRequestAwareAuthenticationSuccessHandler oAuth2LoginSuccessHandler(){
		return new OAuth2LoginSuccessHandler();
	}
	
	@Bean
	public SavedRequestAwareAuthenticationSuccessHandler databaseLoginSuccessHandler(){
		return new DatabaseLoginSuccessHandler2();
	}
	
	@Bean
	public DefaultOAuth2UserService oauth2UserService() {
		return new customerOauth2UserService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomerUserDetailService();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(requests -> requests
    			.antMatchers("/customer","/account_details","/cart").authenticated()
    			
    			.anyRequest().permitAll())
        .formLogin(login ->login
        		.loginPage("/login")
        		.usernameParameter("email")
        		.successHandler(databaseLoginSuccessHandler())
        		.permitAll())
        .oauth2Login(oauth2 -> oauth2
        		.loginPage("/login")
        		.userInfoEndpoint()
        		.userService(oauth2UserService())
        		.and()
        		.successHandler(oAuth2LoginSuccessHandler()))
        .logout(logout -> logout
        		.permitAll())
        .rememberMe(remember->remember.key("LeTrangNguyen1308"));
    			
				
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
	     authProvider.setPasswordEncoder(passwordEncoder());
	  
	     return authProvider;
	 }
}
