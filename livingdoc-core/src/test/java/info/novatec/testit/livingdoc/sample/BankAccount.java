/**
 * Copyright (c) 2008 Pyxis Technologies inc.
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
package info.novatec.testit.livingdoc.sample;

public abstract class BankAccount {
    private AccountType type;
    private Money balance = Money.ZERO;
    private String number;
    private Owner owner;

    public boolean frozen;

    public BankAccount(AccountType accountType, String number, Owner owner) {
        this.number = number;
        this.type = accountType;
        this.owner = owner;
    }

    public abstract void checkFunds(Money amount) throws Exception;

    public Money withdraw(Money amount, WithdrawType withdrawType) throws Exception {
        Money limit = type.limitFor(withdrawType);
        if ( ! AccountType.isNoLimit(limit) && amount.strictlyGreaterThan(limit)) {
            throw new Exception("Limit overpassed");
        }
        Money fees = type.feesFor(withdrawType);
        return withdraw(amount.plus(fees));
    }

    public Money withdraw(Money amount) throws Exception {
        checkNotFrozen();
        checkFunds(amount);
        balance = balance.minus(amount);
        return balance;
    }

    private void checkNotFrozen() throws Exception {
        if (frozen) {
            throw new Exception("Acccount frozen!");
        }
    }

    public String getNumber() {
        return number;
    }

    public Money deposit(Money amount) {
        balance = balance.plus(amount);
        return balance;
    }

    public Money getBalance() {
        return balance;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void freeze() {
        frozen = true;
    }

    public AccountType getType() {
        return type;
    }

    public Owner getOwner() {
        return owner;
    }

    public String getOwnerName() {
        return owner.getFullName();
    }
}
