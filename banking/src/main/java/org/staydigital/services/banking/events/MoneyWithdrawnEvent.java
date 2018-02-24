package org.staydigital.services.banking.events;

public class MoneyWithdrawnEvent {

	public final String id;
	public final double amount;

	public MoneyWithdrawnEvent(final String id, final double amount) {
		this.id = id;
		this.amount = amount;
	}
}