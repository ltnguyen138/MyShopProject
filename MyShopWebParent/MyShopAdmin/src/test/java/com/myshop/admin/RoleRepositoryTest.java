package com.myshop.admin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.myshop.admin.user.RoleRepository;
import com.myshop.admin.user.UserRepository;
import com.myshop.common.entity.Role;
import com.myshop.common.entity.User;





@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {
	@Autowired
	private RoleRepository rolerepository;
	@Autowired
	private UserRepository userrepository;
	
	@Test
	public void testCreateFirstRole() {
		Role roleAdmin= new Role("Admin", "manage everything");
		Role saverole=rolerepository.save(roleAdmin);
		assertThat(saverole.getId()).isGreaterThan(0);
	}
	@Test
	public void testCreateRole() {
		Role roleSalesperson = new Role("Salesperson", "manage product price, "
				+ "customers, shipping, orders and sales report");
		
		Role roleEditor = new Role("Editor", "manage categories, brands, "
				+ "products, articles and menus");
		
		Role roleShipper = new Role("Shipper", "view products, view orders "
				+ "and update order status");
		
		Role roleAssistant = new Role("Assistant", "manage questions and reviews");
		
		rolerepository.saveAll(List.of(roleSalesperson, roleEditor, roleShipper, roleAssistant));
	}
	@Test
	public void test() {
		int n=1;
		int p=4;
		Pageable pa=PageRequest.of(n, p);
		Page<User>page=userrepository.findAll(pa);
		List<User>l=page.getContent();
		l.forEach(u->System.out.print(u));
		assertThat(l.size()).isEqualTo(p);
	}

}
