package info.novatec.testit.livingdoc.samples.application.bank;

import java.util.EnumMap;


public enum AccountType {
    SAVINGS ( "savings", Money.$(200f), Money.$(200f), null, Money.$(1.5f), Money.$(2.5f), Money.$(0.5f) ),
    CHECKING ( "checking", Money.$(200f), Money.$(200f), null, Money.$(0), Money.$(2.50f), Money.$(0) );

    private final String description;

    private EnumMap<WithdrawType, Money> limits;
    private EnumMap<WithdrawType, Money> fees;

    private AccountType(String description, Money limitBankMachine, Money limitInteracMachine, Money limitPersonalCheck,
        Money feesBankMachine, Money feesInteracMachine, Money feesPersonalCheck) {
        this.description = description;
        limits = new EnumMap<WithdrawType, Money>(WithdrawType.class);

        limits.put(WithdrawType.ATM, limitBankMachine);
        limits.put(WithdrawType.INTERAC, limitInteracMachine);
        limits.put(WithdrawType.PERSONAL_CHECK, limitPersonalCheck);

        fees = new EnumMap<WithdrawType, Money>(WithdrawType.class);
        fees.put(WithdrawType.ATM, feesBankMachine);
        fees.put(WithdrawType.INTERAC, feesInteracMachine);
        fees.put(WithdrawType.PERSONAL_CHECK, feesPersonalCheck);

    }

    @Override
    public String toString() {
        return description;
    }

    public Money limitFor(WithdrawType withdrawType) {
        return limits.get(withdrawType);
    }

    public Money feesFor(WithdrawType withdrawType) {
        return fees.get(withdrawType);
    }

    public static boolean isNoLimit(Money limit) {
        return limit == null;
    }

}
