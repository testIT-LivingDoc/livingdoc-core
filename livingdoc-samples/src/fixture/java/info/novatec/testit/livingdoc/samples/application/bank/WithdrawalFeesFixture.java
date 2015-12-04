package info.novatec.testit.livingdoc.samples.application.bank;

import info.novatec.testit.livingdoc.samples.application.bank.AccountType;
import info.novatec.testit.livingdoc.samples.application.bank.Money;
import info.novatec.testit.livingdoc.samples.application.bank.WithdrawType;

public class WithdrawalFeesFixture 
{
	private AccountType accountType; 	 
	private WithdrawType withdrawType;
	
	public AccountType getAccountType() 
	{
		return accountType;
	}

	public void setAccountType(AccountType accountType) 
	{
		this.accountType = accountType;
	}

	public WithdrawType getWithdrawType() 
	{
		return withdrawType;
	}

	public void setWithdrawType(WithdrawType withdrawType) 
	{
		this.withdrawType = withdrawType;
	}

	public Money getFees() 
	{
		return accountType.feesFor(withdrawType);
	}
}
