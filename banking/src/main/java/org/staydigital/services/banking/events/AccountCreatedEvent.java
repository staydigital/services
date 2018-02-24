package org.staydigital.services.banking.events;

public class AccountCreatedEvent {

	public final String id;
	public final String accountCreator;
	public final double balance;

	public AccountCreatedEvent(final String id, final String accountCreator, final double balance) {
		this.id = id;
		this.accountCreator = accountCreator;
		this.balance = balance;
	}
}
