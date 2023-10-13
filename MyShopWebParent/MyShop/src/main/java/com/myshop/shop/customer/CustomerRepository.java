package com.myshop.shop.customer;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.myshop.common.entity.AuthenticationType;
import com.myshop.common.entity.Customer;

@Repository
@Transactional
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
	Optional<Customer> findById(Integer id);
	Optional<Customer>  findByEmail(String email);
	Optional<Customer> findByVerificationCode(String verificationCode);
	Optional<Customer>   findByResetPasswordToken(String resetPasswordToken);
	
	@Modifying
	@Query(value="UPDATE Customer c SET c.enabled=true,c.verificationCode=null WHERE c.id=:id")
	public void updateEnableByVerifyTrue(@Param("id") Integer id);
	
	@Modifying
	@Query(value="UPDATE Customer c SET c.authenticationType = :type WHERE c.id=:id")
	public void updateAuthenticationTypeById(@Param("id") Integer id, @Param("type") AuthenticationType type);
}
