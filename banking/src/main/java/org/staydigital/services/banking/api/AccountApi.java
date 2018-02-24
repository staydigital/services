package org.staydigital.services.banking.api;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.staydigital.services.banking.aggregates.BankAccount;
import org.staydigital.services.banking.commands.CloseAccountCommand;
import org.staydigital.services.banking.commands.CreateAccountCommand;
import org.staydigital.services.banking.commands.DepositMoneyCommand;
import org.staydigital.services.banking.commands.WithdrawMoneyCommand;

@RequestMapping("/accounts")
@RestController
public class AccountApi {

	private final CommandGateway commandGateway;
	
	public AccountApi(final CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}
	
    static class AccountOwner {
		public String name;
	}
    
    private Map<String, Double> accountQueryModel = new HashMap<>();
    
	@PostMapping
	public CompletableFuture<String> createAccount(@RequestBody final AccountOwner owner) {
//		String id = UUID.randomUUID().toString();
		String id = "abc";
		return commandGateway.send(new CreateAccountCommand(id, owner.name));
	}
	
	@GetMapping(path = "{id}/balance")
	public double checkBalance(@PathVariable final String id) {
		return accountQueryModel.get(id).doubleValue();
	}
	
	@PutMapping(path = "{id}/balance")
	public CompletableFuture<String> deposit(@RequestBody final double amount, @PathVariable final String id) {
		double _amount = amount;
		if (!accountQueryModel.containsKey(id)) {
			accountQueryModel.put(id, new Double(0));
		}
		if (amount > 0) {
			accountQueryModel.put(id, accountQueryModel.get(id) + _amount);
			return commandGateway.send(new DepositMoneyCommand(id, amount));
		} else {
			_amount = _amount * -1;
			accountQueryModel.put(id, accountQueryModel.get(id) + _amount);
			return commandGateway.send(new WithdrawMoneyCommand(id, -amount));
		}
	}
	
	@DeleteMapping("{id}")
	public CompletableFuture<String> delete(@PathVariable final String id) {
		return commandGateway.send(new CloseAccountCommand(id));
	}
	
	@ExceptionHandler(AggregateNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void notFound() {
		
	}
	
	@ExceptionHandler(BankAccount.InsufficientBalanceException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String insufficientBalance(BankAccount.InsufficientBalanceException exception) {
	    return exception.getMessage();
	}
}
