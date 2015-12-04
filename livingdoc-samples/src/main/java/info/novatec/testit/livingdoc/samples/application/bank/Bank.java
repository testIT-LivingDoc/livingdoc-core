package info.novatec.testit.livingdoc.samples.application.bank;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;


@Singleton
public class Bank {
    private Map<String, BankAccount> accounts;

    public Bank() {
        accounts = new HashMap<String, BankAccount>();
    }

    public boolean hasAccount(String accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    public BankAccount getAccount(String accountNumber) throws NoSuchAccountException {
        if ( ! hasAccount(accountNumber)) {
            throw new NoSuchAccountException(accountNumber);
        }
        return accounts.get(accountNumber);
    }

    public SavingsAccount openSavingsAccount(String number, Owner owner) {
        if (hasAccount(number)) {
            return null;
        }

        SavingsAccount account = new SavingsAccount(number, owner);
        accounts.put(number, account);
        return account;
    }

    public CheckingAccount openCheckingAccount(String number, Owner owner) {
        if (hasAccount(number)) {
            return null;
        }

        CheckingAccount account = new CheckingAccount(number, owner);
        accounts.put(number, account);
        return account;
    }

    public Money deposit(Money amount, String number) throws Exception {
        BankAccount account = accounts.get(number);
        return account.deposit(amount);
    }

    public Money withdraw(Money amount, String number, WithdrawType type) throws Exception {
        BankAccount account = accounts.get(number);
        return account.withdraw(amount, type);
    }

    public void freezeAccount(String number) {
        BankAccount account = accounts.get(number);
        account.freeze();
    }

    public Collection<BankAccount> getAccounts() {
        return Collections.unmodifiableCollection(accounts.values());
    }

    public void transfer(String numberFrom, String numberTo, Money amountToTransfert) throws Exception {
        if ( ! hasAccount(numberFrom)) {
            throw new NoSuchAccountException(numberFrom);
        }
        if ( ! hasAccount(numberTo)) {
            throw new NoSuchAccountException(numberTo);
        }

        BankAccount accountFrom = accounts.get(numberFrom);
        BankAccount accountTo = accounts.get(numberTo);

        if (accountFrom.getOwner().getFirstName().equals(accountTo.getOwner().getFirstName()) && accountFrom.getOwnerName()
            .equals(accountTo.getOwnerName())) {
            accountFrom.withdraw(amountToTransfert);
            accountTo.deposit(amountToTransfert);
        } else {
            throw new Exception("Can't transfer from not owned account !");
        }
    }
}
