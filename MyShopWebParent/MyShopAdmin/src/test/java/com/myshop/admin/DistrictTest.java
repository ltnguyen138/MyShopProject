package com.myshop.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.myshop.admin.setting.district.DistrictRepostiory;
import com.myshop.common.entity.District;
import com.myshop.common.entity.Province;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class DistrictTest {
	@Autowired DistrictRepostiory districtRepostiory;
	@Autowired TestEntityManager entityManager;
	
	@Test
	public void testCreate() {
		Integer countryId = 1;
		Province province = entityManager.find(Province.class, countryId);
		

		District state = districtRepostiory.save(new District());
		

	}
	
}
