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
package info.novatec.testit.livingdoc.interpreter;

import static info.novatec.testit.livingdoc.Assertions.assertAnnotatedException;
import static info.novatec.testit.livingdoc.Assertions.assertNotAnnotated;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.document.FakeSpecification;
import info.novatec.testit.livingdoc.interpreter.flow.scenario.AccountAlreadyExistException;
import info.novatec.testit.livingdoc.interpreter.flow.scenario.Bank;
import info.novatec.testit.livingdoc.interpreter.flow.scenario.BankFixture;
import info.novatec.testit.livingdoc.interpreter.flow.scenario.Expectation;
import info.novatec.testit.livingdoc.interpreter.flow.scenario.Money;
import info.novatec.testit.livingdoc.interpreter.flow.scenario.Owner;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.util.Tables;


public class ScenarioInterpreterTest {
    private ScenarioInterpreter interpreter;
    private Bank bank;
    private BankFixture fixture;

    @Before
    public void setUp() {
        bank = new Bank();
        fixture = mock(BankFixture.class);
        interpreter = new ScenarioInterpreter(new PlainOldFixture(new BankFixture(bank, fixture)));
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(fixture);
    }

    @Test
    public void checkDispatchCallForGivenScenario() {
        Tables tables = tables("I have a checking account ABC123 under the name of Bart Simpson");

        FakeSpecification spec = new FakeSpecification(tables);
        interpreter.interpret(spec);

        ArgumentCaptor<Owner> ownerCaptor = ArgumentCaptor.forClass(Owner.class);
        verify(fixture).openCheckingAccount(eq("ABC123"), ownerCaptor.capture());

        assertEquals("Bart", ownerCaptor.getValue().getFirstName());
        assertEquals("Simpson", ownerCaptor.getValue().getLastName());
        assertEquals(new Statistics(0, 0, 0, 0), spec.stats());
    }

    @Test
    public void checkDispatchCallForGivenScenarioWithIgnoredException() throws AccountAlreadyExistException {
        Tables tables = tables("1: I already have a checking account ABC123 under the name of Bart Simpson");

        FakeSpecification spec = new FakeSpecification(tables);
        interpreter.interpret(spec);

        ArgumentCaptor<Owner> ownerCaptor = ArgumentCaptor.forClass(Owner.class);
        verify(fixture).createCheckingAccount(eq("ABC123"), ownerCaptor.capture());

        assertEquals("Bart", ownerCaptor.getValue().getFirstName());
        assertEquals("Simpson", ownerCaptor.getValue().getLastName());
        assertEquals(new Statistics(0, 0, 0, 0), spec.stats());

        assertNotAnnotated(tables.at(0, 0, 0));
        assertNotAnnotated(tables.at(0, 1, 0));
    }

    @Test
    public void checkDispatchCallForGivenScenarioWhenIgnoredExceptionDoesNotMatch() throws AccountAlreadyExistException {
        Tables tables = tables("2: I already have a checking account ABC123 under the name of Bart Simpson");

        FakeSpecification spec = new FakeSpecification(tables);
        interpreter.interpret(spec);

        ArgumentCaptor<Owner> ownerCaptor = ArgumentCaptor.forClass(Owner.class);
        verify(fixture).createCheckingAccount(eq("ABC123"), ownerCaptor.capture());

        assertEquals("Bart", ownerCaptor.getValue().getFirstName());
        assertEquals("Simpson", ownerCaptor.getValue().getLastName());
        assertEquals(new Statistics(0, 0, 1, 0), spec.stats());

        assertNotAnnotated(tables.at(0, 0, 0));
        assertAnnotatedException(tables.at(0, 1, 0));
    }

    @Test
    public void checkDispatchCallForThenScenario() throws Exception {
        bank.openCheckingAccount("ABC123", new Owner("Bart", "Simpson"));

        Tables tables = tables("The balance of account ABC123 is $0");

        FakeSpecification spec = new FakeSpecification(tables);
        interpreter.interpret(spec);

        ArgumentCaptor<Expectation> expectationCaptur = ArgumentCaptor.forClass(Expectation.class);
        verify(fixture).theBalanceOfAccount(eq("ABC123"), expectationCaptur.capture());

        assertEquals(money(0), expectationCaptur.getValue().getActual());
        assertTrue(expectationCaptur.getValue().meets());
        // ?assertEquals(new Statistics(1, 0, 0, 0), spec.stats());
    }

    @Test
    public void checkDispatchCallForThenScenarioWhenWrongNumberOfParameters() throws Exception {
        bank.openCheckingAccount("ABC123", new Owner("Bart", "Simpson"));

        Tables tables = tables("Wrong number of parameters for values $0 and $100");

        FakeSpecification spec = new FakeSpecification(tables);
        interpreter.interpret(spec);

        ArgumentCaptor<Expectation> expectationCaptur = ArgumentCaptor.forClass(Expectation.class);
        verify(fixture, never()).checkNumberOfParameters(expectationCaptur.capture());

        assertEquals(new Statistics(0, 0, 1, 0), spec.stats());
    }

    @Test
    public void checkDispatchCallForThenScenarioWithMultipleParameters() throws Exception {
        bank.openCheckingAccount("ABC123", new Owner("Bart", "Simpson"));

        Tables tables = tables("The balance of account ABC123 is $0 ($1)");

        FakeSpecification spec = new FakeSpecification(tables);
        interpreter.interpret(spec);

        ArgumentCaptor<Expectation> expectationCaptur1 = ArgumentCaptor.forClass(Expectation.class);
        ArgumentCaptor<Expectation> expectationCaptur2 = ArgumentCaptor.forClass(Expectation.class);
        verify(fixture).theBalanceOfAccount(eq("ABC123"), expectationCaptur1.capture(), expectationCaptur2.capture());

        assertEquals(money(0), expectationCaptur1.getValue().getActual());
        assertTrue(expectationCaptur1.getValue().meets());

        assertEquals(money(1), expectationCaptur2.getValue().getActual());
        assertTrue(expectationCaptur2.getValue().meets());
    }

    @Test
    public void checkDispatchCallForWhenScenario() throws Exception {
        bank.openCheckingAccount("ABC123", new Owner("Bart", "Simpson"));

        Tables tables = tables("I deposit $100 in account ABC123");

        FakeSpecification spec = new FakeSpecification(tables);
        interpreter.interpret(spec);

        verify(fixture).deposit(money(100), "ABC123");

        // ?assertEquals(new Statistics(1, 0, 0, 0), spec.stats());
    }

    @Test
    public void checkDispatchCallForCheckScenario() throws Exception {
        bank.openCheckingAccount("ABC123", new Owner("Bart", "Simpson"));
        bank.deposit(money(100), "ABC123");

        when(fixture.cannotWithdraw(money(2), "ABC123")).thenReturn(false);
        when(fixture.withdraw(money(999), "ABC123")).thenThrow(new RuntimeException("ex"));

        Tables tables = tables("I cannot withdraw $125 of account ABC123",
            "I cannot withdraw $2 of account ABC123" /* wrong */, "I can withdraw $25 of account ABC123",
            "I withdraw $999 of account ABC123 (ex)" /* exception */);

        FakeSpecification spec = new FakeSpecification(tables);
        interpreter.interpret(spec);

        verify(fixture).cannotWithdraw(money(125), "ABC123");
        verify(fixture).cannotWithdraw(money(2), "ABC123");
        verify(fixture).canWithdraw(money(25), "ABC123");
        verify(fixture).withdraw(money(999), "ABC123");

        assertEquals(new Statistics(2, 1, 1, 0), spec.stats());
    }

    @Test
    public void checkDispatchCallForCheckScenarioThrowingException() throws Exception {
        Tables tables = tables("Check for exception!");

        FakeSpecification spec = new FakeSpecification(tables);
        interpreter.interpret(spec);

        assertEquals(new Statistics(0, 0, 1, 0), spec.stats());

        assertNotAnnotated(tables.at(0, 0, 0));
        assertAnnotatedException(tables.at(0, 1, 0));
    }

    @Test
    public void checkDispatchCallForDisplayScenario() throws Exception {
        bank.openCheckingAccount("ABC123", new Owner("Bart", "Simpson"));
        bank.deposit(money(100), "ABC123");

        Tables tables = tables("Show the balance of account ABC123");

        FakeSpecification spec = new FakeSpecification(tables);
        interpreter.interpret(spec);

        verify(fixture).getBalanceOfAccount("ABC123");

        assertEquals(new Statistics(0, 0, 0, 0), spec.stats());
    }

    @Test
    public void checkFullScenario() {
        Tables tables = tables("I have a checking account ABC123 under the name of Bart Simpson",
            "The balance of account ABC123 is $10", "The balance of account ABC123 is $0", "A method not associated to an @",
            "I deposit $100 in account ABC123", "The balance of account ABC123 is $100",
            "I cannot withdraw $125 of account ABC123", "I can withdraw $25 of account ABC123",
            "I withdraw $999 of account ABC123 (ex)", "Show the balance of account ABC123");

        FakeSpecification spec = new FakeSpecification(tables);
        interpreter.interpret(spec);

        reset(fixture);
    }

    private Tables tables(String... scenarios) {
        StringBuilder sb = new StringBuilder();

        sb.append("[").append(ScenarioInterpreter.class.getName()).append("][mock]\n");

        for (String scenario : scenarios) {
            sb.append("[").append(scenario).append("]\n");
        }

        return Tables.parse(sb.toString());
    }

    private static Money money(int value) {
        return new Money(new BigDecimal(value));
    }
}
