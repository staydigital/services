package org.staydigital.services.banking.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class CloseAccountCommand {

	@TargetAggregateIdentifier
	public final String id;
	
	public CloseAccountCommand(final String id) {
		this.id = id;
	}
}
