package info.novatec.testit.livingdoc.samples.application.bank;

public class CheckingAccount extends BankAccount {
    private Money maxCredit = Money.ZERO;

    public CheckingAccount(String number, Owner owner) {
        super(AccountType.CHECKING, number, owner);
    }

    @Override
    public void checkFunds(Money amount) throws Exception {
        if (getBalance().plus(maxCredit).lowerThan(amount)) {
            throw new Exception("Not enougth credit !");
        }
    }

    public void setCreditLine(Money credit) {
        this.maxCredit = credit;
    }

    public Money limitFor(WithdrawType type) {
        return AccountType.CHECKING.limitFor(type);
    }

}
