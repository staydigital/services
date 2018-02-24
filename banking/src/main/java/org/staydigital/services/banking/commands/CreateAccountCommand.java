package org.staydigital.services.banking.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class CreateAccountCommand {

	@TargetAggregateIdentifier
	public final String id;
	public final String accountCreator;
	
	public CreateAccountCommand(final String id, final String accountCreator) {
		this.id = id;
		this.accountCreator = accountCreator;
	}
}
