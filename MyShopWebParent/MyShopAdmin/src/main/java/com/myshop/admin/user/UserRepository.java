package com.myshop.admin.user;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.myshop.common.entity.User;




@Transactional
@Repository
public interface UserRepository extends CrudRepository<User,Integer>,PagingAndSortingRepository<User, Integer> {
	public Optional<User>  findByEmail(@Param("email") String email);
	public Optional<User> findById(int id);
	@Modifying
	@Query(value="UPDATE users u SET u.enabled=:enabled WHERE u.id=:id", nativeQuery = true)
	public void updateEnable(@Param("id") Integer id,@Param("enabled") boolean enable);

	@Query(value="SELECT u FROM User u WHERE u.firstName LIKE %:keyword% OR u.email LIKE %:keyword% OR u.lastName LIKE %:keyword% ")
	public Page<User> findKey(@Param("keyword") String keyword, Pageable pageable );
	
}
	