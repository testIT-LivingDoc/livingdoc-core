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
