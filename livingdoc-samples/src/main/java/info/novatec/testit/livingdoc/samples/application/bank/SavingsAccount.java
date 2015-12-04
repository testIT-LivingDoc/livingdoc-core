package info.novatec.testit.livingdoc.samples.application.bank;

public class SavingsAccount extends BankAccount {
    public SavingsAccount(String number, Owner owner) {
        super(AccountType.SAVINGS, number, owner);
    }

    @Override
    public void checkFunds(Money amount) throws Exception {
        if (getBalance().lowerThan(amount)) {
            throw new Exception("Not enougth money !");
        }
    }
}
