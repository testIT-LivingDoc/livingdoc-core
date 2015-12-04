package info.novatec.testit.livingdoc.samples.application.bank;

import java.util.Collection;

import com.google.inject.Inject;

public class BankFixture
{
    private Bank bank;

    @Inject
    public BankFixture(Bank bank)
    {
    	this.bank = bank;
    }

	public boolean openSavingsAccountUnderTheNameOf(String number, String firstName, String lastName)
    {
        return bank.openSavingsAccount(number, new Owner(firstName, lastName)) != null;
    }

    public boolean openCheckingAccountUnderTheNameOf(String number, String firstName, String lastName)
    {
        return bank.openCheckingAccount(number, new Owner(firstName, lastName)) != null;
    }

    public boolean openAccountUnderTheNameOf(AccountType type, String number, String firstName, String lastName)
    {
    	if (AccountType.SAVINGS == type)
    	{
    		return openSavingsAccountUnderTheNameOf(number, firstName, lastName);
    	}
    	else if (AccountType.CHECKING == type)
    	{
    		return openCheckingAccountUnderTheNameOf(number, firstName, lastName);
    	}
    	return false;
    }

    public Money thatBalanceOfAccountIs(String accountNumber) throws Exception
    {
        BankAccount account = bank.getAccount(accountNumber);
        return account.getBalance();
    }

    public boolean depositInAccount(Money amount, String accountNumber) throws Exception
    {
        bank.deposit(amount, accountNumber);
		return true;
    }

    public boolean withdrawFromAccount(Money amount, String accountNumber) throws Exception
    {
        return withdrawFromAccountUsing( amount, accountNumber, WithdrawType.ATM );
    }

    public boolean withdrawFromAccountUsing(Money amount, String accountNumber, WithdrawType withdrawType) throws Exception
    {
        try
        {
            bank.withdraw(amount, accountNumber, withdrawType);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    public Collection getOpenedAccounts()
    {
        return bank.getAccounts();
    }

    public void freezeAccount(String accountNumber)
    {
        bank.freezeAccount(accountNumber);
    }

	public boolean createAccountForWithBalanceOf(AccountType type, String number, String firstName, String lastName, Money balance) throws Exception
	{
        BankAccount account = (type == AccountType.SAVINGS) ?
            bank.openSavingsAccount(number, new Owner(firstName, lastName)) :
	    	bank.openCheckingAccount(number, new Owner(firstName, lastName));

        account.deposit(balance);
        return true;
	}
	
	public boolean transferFromAccountToAccount(Money amountToTransfer, String fromAccountNumber, String toAccountNumber) throws Exception
	{
		try
		{
			bank.transfer(fromAccountNumber, toAccountNumber, amountToTransfer);
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}
	
}
