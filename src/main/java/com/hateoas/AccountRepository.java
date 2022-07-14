package com.hateoas;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Integer> {

	@Modifying
	@Transactional
	@Query("update Account a set a.balance=a.balance + ?1 where a.id=?2")
	public void updateBalance(float amount, Integer id);

}
