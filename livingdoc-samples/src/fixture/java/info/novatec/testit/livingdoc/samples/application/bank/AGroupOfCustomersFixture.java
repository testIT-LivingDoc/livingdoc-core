package info.novatec.testit.livingdoc.samples.application.bank;

import com.google.inject.Inject;
import info.novatec.testit.livingdoc.samples.application.bank.AccountType;
import info.novatec.testit.livingdoc.samples.application.bank.Bank;
import info.novatec.testit.livingdoc.samples.application.bank.Money;
import info.novatec.testit.livingdoc.samples.application.bank.Owner;

public class AGroupOfCustomersFixture 
{
	public AccountType type;
	public String number, firstName, lastName;
	public Money balance;
	public Bank bank;
	
	@Inject
	public AGroupOfCustomersFixture(Bank bank)
	{
		this.bank= bank;
	}

	public void enterRow()
	{
		if(AccountType.SAVINGS == type)
			bank.openSavingsAccount(number, new Owner(firstName, lastName)).deposit(balance);
		
		else if(AccountType.CHECKING == type)
			bank.openCheckingAccount(number, new Owner(firstName, lastName)).deposit(balance);
	}
}
