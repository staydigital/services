package org.staydigital.services.banking.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class DepositMoneyCommand {

	@TargetAggregateIdentifier
	public final String id;
	public final double amount;
	
	public DepositMoneyCommand(final String id, final double amount) {
		this.id = id;
		this.amount = amount;
	}
}
