package info.novatec.testit.livingdoc.samples.application.mortgage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


public class MortgageBalanceCalculatorTest {
    private float commercialValue;
    private float purchasedPrice;
    private float mortgageAllowance;
    private float minCashDown;

    @Before
    public void setUp() {
        purchasedPrice = 100000;
        commercialValue = 100000;
        mortgageAllowance = 100000;
        minCashDown = mortgageAllowance * MortgageBalanceCalculator.MIN_CASH_DOWN_RATIO;
    }

    @Test
    public void testThatTheMortgageAllowanceIsToppedByTheCommercialValue() {
        float expetedMortgageAllowance = Math.min(commercialValue * MortgageBalanceCalculator.MAX_MORTGAGE_RATIO,
            purchasedPrice);
        assertTrue(expetedMortgageAllowance == MortgageBalanceCalculator.getMortgageAllowance(commercialValue,
            purchasedPrice));
    }

    @Test
    public void testThatTheCashDownIsInsufficient() {
        float insufficientCashDown = minCashDown - 1;
        assertTrue(MortgageBalanceCalculator.isInsufficient(mortgageAllowance, insufficientCashDown));

        float sufficientCashDown = minCashDown + 1;
        assertFalse(MortgageBalanceCalculator.isInsufficient(mortgageAllowance, sufficientCashDown));
    }

    public void testThatTheMortgageBalanceIsIncreasedIfTheCashDownIsInsufficient() {
        float insufficientCashDown = minCashDown - 1;
        float balanceBeforeFee = mortgageAllowance - insufficientCashDown;
        float expetedBalance = balanceBeforeFee * ( 1 + MortgageBalanceCalculator.MIN_CASH_DOWN_FEE_RATIO );

        assertTrue(expetedBalance == MortgageBalanceCalculator.evaluate(mortgageAllowance, insufficientCashDown));
    }

    @Test
    public void testThatTheMortgageBalanceIsNotImpactedIfTheCashDownIsSufficient() {
        float insufficientCashDown = minCashDown + 1;
        float expetedBalance = mortgageAllowance - insufficientCashDown;

        assertTrue(expetedBalance == MortgageBalanceCalculator.evaluate(mortgageAllowance, insufficientCashDown));
    }

    @Test
    public void testThatTheFeesAreAddedToTheMortgageBalanceInTheBankPayYourBillsOption() {
        float bienvenueTax = 1500;
        float bigNotaryFees = MortgageBalanceCalculator.MAX_NOTARY_FEE + 1;
        float expetedBalance = mortgageAllowance + MortgageBalanceCalculator.MAX_NOTARY_FEE + bienvenueTax;
        assertTrue(MortgageBalanceCalculator.MAX_NOTARY_FEE == MortgageBalanceCalculator.getPayedNotaryFee(bigNotaryFees));
        assertTrue(expetedBalance == MortgageBalanceCalculator.reevaluateBankPayYourBillsOption(mortgageAllowance,
            bigNotaryFees, bienvenueTax));

        float smallNotaryFees = MortgageBalanceCalculator.MAX_NOTARY_FEE - 1;
        expetedBalance = mortgageAllowance + smallNotaryFees + bienvenueTax;
        assertTrue(smallNotaryFees == MortgageBalanceCalculator.getPayedNotaryFee(smallNotaryFees));
        assertTrue(expetedBalance == MortgageBalanceCalculator.reevaluateBankPayYourBillsOption(mortgageAllowance,
            smallNotaryFees, bienvenueTax));
    }
}
