package info.novatec.testit.livingdoc.alias;

import info.novatec.testit.livingdoc.reflect.annotation.Alias;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


@FixtureClass("FluentAliasCalculator")
public class AliasAlternateCalculatorHolder {

    private static AliasAlternateCalculator calculator = new AliasAlternateCalculator();

    public AliasAlternateCalculatorHolder withA(int c) {
        calculator.a = c;
        return this;
    }

    public AliasAlternateCalculatorHolder withB(int b) {
        calculator.setB(b);
        return this;
    }

    @Alias("that sum is")
    public int sum() {
        return calculator.sum();
    }

    public double divideAWith(int divider) {
        return calculator.a / divider;
    }

    public boolean thatIHaveMoney(int money) {
        return money > 0;
    }

}
