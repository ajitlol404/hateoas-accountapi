package com.hateoas;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class AccountService {

	private AccountRepository accountRepository;

	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public List<Account> listAll() {
		return accountRepository.findAll();
	}

	public Account get(Integer id) {
		return accountRepository.findById(id).get();
	}

	public Account save(Account account) {
		return accountRepository.save(account);
	}

	public Account deposit(float amount, Integer id) {
		accountRepository.updateBalance(amount, id);
		return accountRepository.findById(id).get();
	}

	public Account withdraw(float amount, Integer id) {
		accountRepository.updateBalance(-amount, id);
		return accountRepository.findById(id).get();
	}

	public void delete(Integer id) throws AccountNotFoundException {
		if (!accountRepository.existsById(id)) {
			throw new AccountNotFoundException();
		}
		accountRepository.deleteById(id);
	}

}
