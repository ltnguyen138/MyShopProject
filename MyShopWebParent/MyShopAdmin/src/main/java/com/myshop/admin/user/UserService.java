package com.myshop.admin.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.myshop.common.entity.Role;
import com.myshop.common.entity.User;



@Service
public interface UserService {
	public static final int USERS_PER_PAGE = 6;
	public List<User> listAll();
	public List<Role> listRoles();
	public User saveUser(User user);
	public User getUserByEmail(String Email);
	public boolean isEmailUnique(String Email, Integer id);
	public User getUserById(int id) throws UserNotFoundException;
	public void deleteUser(Integer id)throws UserNotFoundException;
	public void updateEnble(Integer id, boolean enabled);
	public Page<User> listByPage(int pageNum, String sortField,String sortDir,String keyword);
	public User updateAccount(User userFromForm) throws UserNotFoundException;
}
