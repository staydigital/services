package org.staydigital.services.banking.aggregates;

import java.io.Serializable;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.util.Assert;
import org.staydigital.services.banking.commands.CloseAccountCommand;
import org.staydigital.services.banking.commands.CreateAccountCommand;
import org.staydigital.services.banking.commands.DepositMoneyCommand;
import org.staydigital.services.banking.commands.WithdrawMoneyCommand;
import org.staydigital.services.banking.events.AccountClosedEvent;
import org.staydigital.services.banking.events.AccountCreatedEvent;
import org.staydigital.services.banking.events.MoneyDepositedEvent;
import org.staydigital.services.banking.events.MoneyWithdrawnEvent;

@Aggregate
public class BankAccount implements Serializable {

	private static final long serialVersionUID = 1L;

	@AggregateIdentifier
	private String id;
	private double balance;
	private String owner;

	public static class InsufficientBalanceException extends RuntimeException {
		InsufficientBalanceException(String message) {
			super(message);
		}
	}

	public BankAccount() {
	}

	@CommandHandler
	public BankAccount(CreateAccountCommand command) {
		String id = command.id;
		String creator = command.accountCreator;

		Assert.hasLength(id, "Missing id");
		Assert.hasLength(creator, "Missing account creator");

		AggregateLifecycle.apply(new AccountCreatedEvent(id, creator, 0));
	}

	@EventSourcingHandler
	protected void on(final AccountCreatedEvent event) {
		this.id = event.id;
		this.owner = event.accountCreator;
		this.balance = event.balance;
	}

	@CommandHandler
	protected void on(final DepositMoneyCommand command) {
		double amount = command.amount;
		Assert.isTrue(amount > 0.0, "Deposit must be a positive number.");

		AggregateLifecycle.apply(new MoneyDepositedEvent(id, amount));
	}

	@EventSourcingHandler
	protected void in(final MoneyDepositedEvent event) {
		this.balance += event.amount;
	}

	@CommandHandler
	protected void on(final WithdrawMoneyCommand command) {
		double amount = command.amount;
		Assert.isTrue(amount > 0.0, "Withdraw must be a positive number.");

		if (this.balance - amount < 0) {
			throw new InsufficientBalanceException("Insufficient balance.");
		}

		AggregateLifecycle.apply(new MoneyWithdrawnEvent(id, amount));
	}

	@EventSourcingHandler
	protected void on(MoneyWithdrawnEvent event) {
		this.balance -= event.amount;
	}

	@CommandHandler
	protected void on(CloseAccountCommand command) {
		AggregateLifecycle.apply(new AccountClosedEvent(id));
	}

	@EventSourcingHandler
	protected void on(AccountClosedEvent event) {
		AggregateLifecycle.markDeleted();
	}
}
