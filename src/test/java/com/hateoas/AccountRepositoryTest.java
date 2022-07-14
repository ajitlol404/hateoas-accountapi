package com.hateoas;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AccountRepositoryTest {

	@Autowired
	private AccountRepository accountRepository;

	@Test
	public void testAddAccount() {
		Account account = new Account("12345678", 1000);
		Account savedAccount = accountRepository.save(account);

		assertThat(savedAccount).isNotNull();
	}

	@Test
	public void testDepositAccount() {
		Account account = new Account("12345678", 1000);
		Account savedAccount = accountRepository.save(account);
		
		accountRepository.updateBalance(50, savedAccount.getId());
		
		Account updatedAccount = accountRepository.findById(savedAccount.getId()).get();
		
		assertThat(updatedAccount.getBalance()).isEqualTo(50);
	}
}
