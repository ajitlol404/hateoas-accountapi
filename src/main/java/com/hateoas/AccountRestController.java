package com.hateoas;

// Static import
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;;

@RestController
@RequestMapping("/api/account")
public class AccountRestController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private AccountModelAssembler accountModelAssembler;

	@GetMapping
	public ResponseEntity<CollectionModel<Account>> listAll() {
		List<Account> listAccount = accountService.listAll();
		if (listAccount.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

//		for (Account account : listAccount) {
//			account.add(linkTo(methodOn(AccountRestController.class).getOne(account.getId())).withSelfRel());
//			account.add(
//					linkTo(methodOn(AccountRestController.class).deposit(account.getId(), null)).withRel("deposits"));
//			account.add(
//					linkTo(methodOn(AccountRestController.class).withdraw(account.getId(), null)).withRel("withdraw"));
//			account.add(linkTo(methodOn(AccountRestController.class).listAll()).withRel(IanaLinkRelations.COLLECTION));
//		}

		listAccount.stream().map(accountModelAssembler::toModel).collect(Collectors.toList());

		CollectionModel<Account> collectionModel = CollectionModel.of(listAccount);
		collectionModel.add(linkTo(methodOn(AccountRestController.class).listAll()).withSelfRel());

		return new ResponseEntity<>(collectionModel, HttpStatus.OK);

	}

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Account>> getOne(@PathVariable Integer id) {
		try {
			Account account = accountService.get(id);

			EntityModel<Account> entityModel = accountModelAssembler.toModel(account);

			return new ResponseEntity<>(entityModel, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<EntityModel<Account>> add(@RequestBody Account account) {
		Account savedAccount = accountService.save(account);

		EntityModel<Account> entityModel = accountModelAssembler.toModel(savedAccount);
//		savedAccount.add(linkTo(methodOn(AccountRestController.class).getOne(savedAccount.getId())).withSelfRel());
//		savedAccount.add(
//				linkTo(methodOn(AccountRestController.class).deposit(savedAccount.getId(), null)).withRel("deposits"));
//		savedAccount.add(linkTo(methodOn(AccountRestController.class).listAll()).withRel(IanaLinkRelations.COLLECTION));
//		savedAccount.add(
//				linkTo(methodOn(AccountRestController.class).withdraw(savedAccount.getId(), null)).withRel("withdraw"));

		return ResponseEntity
				.created(linkTo(methodOn(AccountRestController.class).getOne(savedAccount.getId())).toUri())
				.body(entityModel);
	}

	@PutMapping
	public ResponseEntity<EntityModel<Account>> replace(@RequestBody Account account) {
		Account updatedAccount = accountService.save(account);

		EntityModel<Account> entityModel = accountModelAssembler.toModel(updatedAccount);

		return new ResponseEntity<>(entityModel, HttpStatus.OK);
	}

	@PatchMapping("/{id}/deposit")
	public ResponseEntity<EntityModel<Account>> deposit(@PathVariable Integer id, @RequestBody Amount amount) {
		Account updatedAccount = accountService.deposit(amount.getAmount(), id);

		EntityModel<Account> entityModel = accountModelAssembler.toModel(updatedAccount);

		return new ResponseEntity<>(entityModel, HttpStatus.OK);
	}

	@PatchMapping("/{id}/withdraw")
	public ResponseEntity<EntityModel<Account>> withdraw(@PathVariable Integer id, @RequestBody Amount amount) {
		Account updatedAccount = accountService.deposit(amount.getAmount(), id);

		EntityModel<Account> entityModel = accountModelAssembler.toModel(updatedAccount);

		return new ResponseEntity<>(entityModel, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		try {
			accountService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (AccountNotFoundException e) {
			return ResponseEntity.notFound().build();
		}

	}
}
