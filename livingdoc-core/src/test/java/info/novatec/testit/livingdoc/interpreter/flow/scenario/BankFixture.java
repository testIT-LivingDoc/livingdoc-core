/**
 * Copyright (c) 2009 Pyxis Technologies inc.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org.
 */
package info.novatec.testit.livingdoc.interpreter.flow.scenario;

import java.math.BigDecimal;

import info.novatec.testit.livingdoc.TypeConversion;


public class BankFixture {
    private final Bank bank;
    private final BankFixture mock;

    static {
        if ( ! TypeConversion.supports(Owner.class)) {
            TypeConversion.register(new OwnerTypeConverter());
        }
    }

    public BankFixture() {
        this(new Bank(), null);
    }

    public BankFixture(Bank bank, BankFixture mock) {
        this.bank = bank;
        this.mock = mock;
    }

    @IgnoredException
    public void ignored() {
        // No implementation needed.
    }

    @Given("I have a checking account ([\\S|\\d]+) under the name of ([\\w|\\s]*)")
    public void openCheckingAccount(String account, Owner owner) {
        if (mock != null) {
            mock.openCheckingAccount(account, owner);
        }
        bank.openCheckingAccount(account, owner);
    }

    @Given("1: I already have a checking account ([\\S|\\d]+) under the name of ([\\w|\\s]*)")
    @IgnoredException({ AccountException.class })
    public void createCheckingAccount(String account, Owner owner) throws AccountAlreadyExistException {
        if (mock != null) {
            mock.createCheckingAccount(account, owner);
        }
        throw new AccountAlreadyExistException(account);
    }

    @Given("2: I already have a checking account ([\\S|\\d]+) under the name of ([\\w|\\s]*)")
    @IgnoredException({ NoSuchAccountException.class })
    public void createAnotherCheckingAccount(String account, Owner owner) throws AccountAlreadyExistException {
        if (mock != null) {
            mock.createCheckingAccount(account, owner);
        }
        throw new AccountAlreadyExistException(account);
    }

    @Then("The balance of account ([\\S|\\d]+) is (\\$\\d+)\\z")
    public void theBalanceOfAccount(String account, Expectation balance) throws NoSuchAccountException {
        if (mock != null) {
            mock.theBalanceOfAccount(account, balance);
        }

        Money accountBalance = bank.getAccount(account).getBalance();
        balance.setActual(accountBalance);
        balance.setDescribe("Hello!");
    }

    @Then("The balance of account ([\\S|\\d]+) is (\\$\\d+) \\((\\$\\d+)\\)\\z")
    // just something to have more than 1 Expectation!
    public void theBalanceOfAccount(String account, Expectation balance, Expectation balancePlusOne)
        throws NoSuchAccountException {

        if (mock != null) {
            mock.theBalanceOfAccount(account, balance, balancePlusOne);
        }

        Money accountBalance = bank.getAccount(account).getBalance();
        balance.setActual(accountBalance);
        balancePlusOne.setActual(accountBalance.plus(new Money(new BigDecimal(1))));
    }

    @Then("Wrong number of parameters for values (\\$\\d+) and (\\$\\d+)")
    public void checkNumberOfParameters(Expectation value1) {
        if (mock != null) {
            mock.checkNumberOfParameters(value1);
        }
    }

    @When("I deposit (\\$\\d+) in account ([\\S|\\d]+)")
    public void deposit(Money amount, String account) throws Exception {
        if (mock != null) {
            mock.deposit(amount, account);
        }
        bank.deposit(amount, account);
    }

    @When("I withdraw (\\$\\d+) of account ([\\S|\\d]+) \\(ex\\)")
    public boolean withdraw(Money amount, String account) throws Exception {

        if (mock != null) {
            mock.withdraw(amount, account);
        }
        // return false;
        throw new Exception("This account has been freezed!");
    }

    @Check("I cannot withdraw (\\$\\d+) of account ([\\S|\\d]+)")
    public boolean cannotWithdraw(Money amount, String account) throws Exception {

        if (mock != null) {
            mock.cannotWithdraw(amount, account);
        }

        try {
            bank.withdraw(amount, account, WithdrawType.ATM);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    @Check("I can withdraw (\\$\\d+) of account ([\\S|\\d]+)")
    public boolean canWithdraw(Money amount, String account) throws Exception {
        if (mock != null) {
            mock.canWithdraw(amount, account);
        }
        bank.withdraw(amount, account, WithdrawType.ATM);
        return true;
    }

    @Check("Check for exception!")
    public void checkForException() {
        throw new IllegalArgumentException("!");
    }

    @Display("Show the balance of account ([\\S|\\d]+)")
    public Money getBalanceOfAccount(String account) throws NoSuchAccountException {

        if (mock != null) {
            mock.getBalanceOfAccount(account);
        }

        return bank.getAccount(account).getBalance();
    }
}
