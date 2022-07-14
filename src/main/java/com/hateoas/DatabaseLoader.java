package com.hateoas;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseLoader {

	private AccountRepository accountRepository;

	public DatabaseLoader(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Bean
	public CommandLineRunner initDatabase() {
		return args -> {
			Account account1 = new Account("12300001", 100);
			Account account2 = new Account("12300002", 150);
			Account account3 = new Account("12300003", 600);

			accountRepository.saveAll(List.of(account1, account2, account3));

			System.out.println("Sample database intialized");
		};
	}

}
