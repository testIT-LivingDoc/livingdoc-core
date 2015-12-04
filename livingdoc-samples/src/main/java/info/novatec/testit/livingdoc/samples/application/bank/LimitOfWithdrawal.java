package info.novatec.testit.livingdoc.samples.application.bank;

public class LimitOfWithdrawal {
    private AccountType accountType;
    private WithdrawType withdrawType;

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public WithdrawType getWithdrawType() {
        return withdrawType;
    }

    public void setWithdrawType(WithdrawType withdrawType) {
        this.withdrawType = withdrawType;
    }

    public Money getLimit() {
        return accountType.limitFor(withdrawType);
    }

}
