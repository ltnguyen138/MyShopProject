package com.myshop.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.myshop.common.entity.Role;
import com.myshop.common.entity.User;



@Component
public class UserServiceImpl implements UserService  {
	
	@Autowired
	private UserRepository userrepository;
	@Autowired
	private RoleRepository rolerepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Override
	public List<User> listAll() {
		return (List<User>) userrepository.findAll();
	}

	@Override
	public List<Role> listRoles() {
		return (List<Role>) rolerepository.findAll();
	}

	@Override
	public User saveUser(User user) {
		boolean isUpdateUser=(user.getId()!=null);
		if(isUpdateUser) {
			User existingUser=userrepository.findById(user.getId()).orElse(null);
			if(user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			}else {
				endcodePassword(user);
			}
			if(user.getPhotos()==null)user.setPhotos(existingUser.getPhotos());
		}else {
			endcodePassword(user);
		}
		
		return userrepository.save(user);
		
	}
	private void endcodePassword(User user) {
		String endcodedPassword=passwordEncoder.encode(user.getPassword());
		user.setPassword(endcodedPassword);
	}

	@Override
	public User getUserByEmail(String Email) {
		User user= userrepository.findByEmail(Email).orElse(null);
		return user;
	}

	@Override
	public boolean isEmailUnique(String Email, Integer id) {
		User user =getUserByEmail(Email);
		if(user==null) {
			return true;
		}
		boolean isCreateNew= (id==null);
		if(isCreateNew) {
			return true;
		}
		else {
			if(user.getId()!=id) {
				return false;
			}
		}
		return true;
	}
	@Override
	public User getUserById(int id) throws UserNotFoundException  {
			User user =userrepository.findById(id).orElseThrow(()-> new UserNotFoundException("Không tìm thấy người dùng có id là: "+id));
			return user;
	}

	@Override
	public void deleteUser(Integer id) throws UserNotFoundException {
		User user =getUserById(id);
		userrepository.delete(user);
	}

	@Override
	public void updateEnble(Integer id, boolean enabled) {
		userrepository.updateEnable(id, enabled);
		
	}

	@Override
	public Page<User> listByPage(int pageNum, String sortField,String sortDir,String keyword) {
		Sort sort=Sort.by(sortField);
		sort=sortDir.equals("asc")?sort.ascending():sort.descending();
		Pageable pageable=PageRequest.of(pageNum-1, USERS_PER_PAGE,sort);
		if (keyword.equals("null")||keyword==null) return userrepository.findAll(pageable);
		return userrepository.findKey(keyword, pageable);
	}

	@Override
	public User updateAccount(User userFromForm) throws UserNotFoundException {
		User userInDB=getUserById(userFromForm.getId());
		if(!userFromForm.getPassword().isEmpty()) {
			userInDB.setPassword(userFromForm.getPassword());
			endcodePassword(userInDB);
		}
		if(userFromForm.getPhotos()!=null) {
			userInDB.setPhotos(userFromForm.getPhotos());
		}
		userInDB.setFirstName(userFromForm.getFirstName());
		userInDB.setLastName(userFromForm.getLastName());
		return userrepository.save(userInDB);
	}
}
