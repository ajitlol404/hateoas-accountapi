package com.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;

@Configuration
public class AccountModelAssembler implements RepresentationModelAssembler<Account, EntityModel<Account>> {

	@Override
	public EntityModel<Account> toModel(Account account) {
		EntityModel<Account> accountModel = EntityModel.of(account);

		accountModel.add(linkTo(methodOn(AccountRestController.class).getOne(account.getId())).withSelfRel());
		accountModel
				.add(linkTo(methodOn(AccountRestController.class).deposit(account.getId(), null)).withRel("deposits"));
		accountModel
				.add(linkTo(methodOn(AccountRestController.class).withdraw(account.getId(), null)).withRel("withdraw"));
		accountModel.add(linkTo(methodOn(AccountRestController.class).listAll()).withRel(IanaLinkRelations.COLLECTION));

		return accountModel;
	}

}
