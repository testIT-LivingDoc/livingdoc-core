/* Copyright (c) 2007 Pyxis Technologies inc.
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
 * http://www.fsf.org. */

package info.novatec.testit.livingdoc.samples.application.bank;

import java.math.BigDecimal;


public class Money {
    public static final Money ZERO = new Money(BigDecimal.ZERO);
    private final BigDecimal dollars;

    public Money(BigDecimal dollars) {
        this.dollars = dollars;
    }

    public static Money parse(String text) {
        return new Money(new BigDecimal(normalize(text)));
    }

    private static String normalize(String text) {
        return text.replaceAll("\\$", "").replaceAll(",", "").replaceAll("\\s", "");
    }

    public static Money dollars(float amount) {
        return new Money(new BigDecimal(amount));
    }

    public static Money $(float amount) {
        return dollars(amount);
    }

    public static Money zero() {
        return dollars(0f);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Money) {
            Money that = ( Money ) other;
            return this.dollars.compareTo(that.dollars) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return dollars.hashCode();
    }

    @Override
    public String toString() {
        return "$" + dollars;
    }

    public Money times(BigDecimal multiplier) {
        return new Money(dollars.multiply(multiplier));
    }

    public Money times(float multiplier) {
        return new Money(dollars.multiply(new BigDecimal(multiplier)));
    }

    public Money minus(Money subtrahend) {
        return new Money(dollars.subtract(subtrahend.dollars));
    }

    public Money plus(Money addend) {
        return new Money(dollars.add(addend.dollars));
    }

    public boolean greaterThan(Money money) {
        return this.dollars.compareTo(money.dollars) >= 0;
    }

    public boolean strictlyGreaterThan(Money money) {
        return this.dollars.compareTo(money.dollars) > 0;
    }

    public boolean lowerThan(Money money) {
        return dollars.compareTo(money.dollars) <= 0;
    }

    public boolean strictlyLowerThan(Money money) {
        return dollars.compareTo(money.dollars) < 0;
    }
}
