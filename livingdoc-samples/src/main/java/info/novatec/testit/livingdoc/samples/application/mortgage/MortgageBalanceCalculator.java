package info.novatec.testit.livingdoc.samples.application.mortgage;

public final class MortgageBalanceCalculator {
    public static final float MAX_NOTARY_FEE = 1200;
    public static final float MIN_CASH_DOWN_RATIO = 0.25f;
    public static final float MIN_CASH_DOWN_FEE_RATIO = 0.025f;
    public static final float MAX_MORTGAGE_RATIO = 0.75f;

    private MortgageBalanceCalculator() {
    }

    public static boolean isInsufficient(float morgageAllowance, float cashDown) {
        return ( cashDown / morgageAllowance ) < MIN_CASH_DOWN_RATIO;
    }

    public static float evaluate(float mortgageAllowance, float cashDown) {
        float mortgageBalance = mortgageAllowance - cashDown;
        if (isInsufficient(mortgageAllowance, cashDown)) {
            mortgageBalance = mortgageBalance * ( 1 + MIN_CASH_DOWN_FEE_RATIO );
        }

        return mortgageBalance;
    }

    public static float reevaluateBankPayYourBillsOption(float mortgageAllowance, float notaryFees, float bienvenueTax) {
        return mortgageAllowance + getPayedNotaryFee(notaryFees) + bienvenueTax;
    }

    public static float getMortgageAllowance(float commercialValue, float purchasedPrice) {
        return Math.min(commercialValue * MAX_MORTGAGE_RATIO, purchasedPrice);
    }

    public static float getPayedNotaryFee(float bigNotaryFee) {
        return Math.min(MAX_NOTARY_FEE, bigNotaryFee);
    }
}
