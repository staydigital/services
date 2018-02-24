package org.staydigital.services.banking.events;

public class MoneyDepositedEvent {

	public final String id;
	public final double amount;

	public MoneyDepositedEvent(final String id, final double amount) {
		this.id = id;
		this.amount = amount;
	}
}
