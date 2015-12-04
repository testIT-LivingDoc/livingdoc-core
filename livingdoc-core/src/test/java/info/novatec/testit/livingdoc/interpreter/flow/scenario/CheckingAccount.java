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
package info.novatec.testit.livingdoc.interpreter.flow.scenario;

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
