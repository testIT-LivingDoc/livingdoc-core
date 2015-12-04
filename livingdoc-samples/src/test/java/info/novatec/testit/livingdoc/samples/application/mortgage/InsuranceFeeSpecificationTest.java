/* Copyright (c) 2006 Pyxis Technologies inc.
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

package info.novatec.testit.livingdoc.samples.application.mortgage;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class InsuranceFeeSpecificationTest {

    @Test
    public void testInsuranceFeeIsProportionalToFinancedAmount() {
        InsuranceFeeSpecification insuranceFee = new InsuranceFeeSpecification(Money.parse("$150,000"));
        assertEquals(Money.parse("$3,500"), insuranceFee.forDownpayment(Money.parse("$10,000")));
    }

    @Test
    public void testFeeIsZeroIfDownpaymentIsOverTheLimit() {
        InsuranceFeeSpecification insuranceFee = new InsuranceFeeSpecification(Money.parse("$150,000"));
        assertEquals(Money.zero(), insuranceFee.forDownpayment(Money.parse("$50,000")));
    }
}
