package info.novatec.testit.livingdoc.interpreter.flow.workflow;

import info.novatec.testit.livingdoc.TypeConversion;
import info.novatec.testit.livingdoc.reflect.annotation.Alias;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;
import info.novatec.testit.livingdoc.sample.Bank;
import info.novatec.testit.livingdoc.sample.CheckingAccount;
import info.novatec.testit.livingdoc.sample.NoSuchAccountException;
import info.novatec.testit.livingdoc.sample.Owner;
import info.novatec.testit.livingdoc.sample.OwnerTypeConverter;
import info.novatec.testit.livingdoc.sample.SavingsAccount;


@FixtureClass(value = "Withdrawing money from account")
public class AccountWithdrawalFixture {
    private final Bank bank;
    private SavingsAccount savingsAccount;
    private CheckingAccount checkingAccount;
    private Owner owner ;
    static {
        if ( ! TypeConversion.supports(Owner.class)) {
            TypeConversion.register(new OwnerTypeConverter());
        }
    }

    public AccountWithdrawalFixture() {
        this(new Bank());
    }

    public AccountWithdrawalFixture(Bank bank) {
        this.bank = bank;
    }

    @Alias("an interested customer")
    public boolean setOwner(Owner owner) {
        this.owner = owner;
        return true;
    }
    
    @Alias("the balance of account is")
    public String getBalance(String account) throws NoSuchAccountException{
        return bank.getAccount(account).getBalance().toString();
    }
    
    
    @Alias("he opens a new savings account")
    public boolean openSavingsAccount(String account) {
        savingsAccount = bank.openSavingsAccount(account, owner);
        return savingsAccount != null;
    }

    @Alias("he opens a new checking account")
    public boolean openCheckingAccount(String account) {
       checkingAccount = bank.openCheckingAccount(account, owner);
       return checkingAccount !=null;
    }

    @Alias("an error occured")
    public boolean errorOccured(){
        return checkingAccount == null && savingsAccount == null;
    }
}
