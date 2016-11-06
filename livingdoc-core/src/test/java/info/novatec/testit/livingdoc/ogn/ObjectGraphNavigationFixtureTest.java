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
package info.novatec.testit.livingdoc.ogn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.Message;
import info.novatec.testit.livingdoc.reflect.NoSuchMessageException;
import info.novatec.testit.livingdoc.reflect.SystemUnderTest;


public class ObjectGraphNavigationFixtureTest {
    private Fixture fixture;
    private Target target;

    @Before
    public void setUp() throws Exception {
        target = new Target();
        fixture = new ObjectGraphNavigationFixture(target);
    }

    @Test
    public void testTargetInstanceReference() throws Exception {
        Object received = fixture.getTarget();
        assertSame(target, received);
    }

    @Test
    public void testFixtoreForShouldReturnOGNFixture() throws Exception {
        Fixture received = fixture.fixtureFor(new Target());

        assertTrue(received instanceof ObjectGraphNavigationFixture);
    }

    @Test
    public void testShouldNotRespondToUnknowMessage() throws Exception {
        assertFalse(fixture.canCheck("unknow method name"));
        assertFalse(fixture.canSend("unknow method name"));
    }

    @Test
    public void testShouldRespondToKnownMessage() throws Exception {
        assertTrue(fixture.canCheck("property"));
        assertTrue(fixture.canSend("property"));
    }

    @Test
    public void testSendsForFieldInFixture() throws Exception {
        Message check = fixture.check("publicfield");
        assertEquals(0, check.send());

        Message send = fixture.send("publicfield");
        send.send("1");
        assertEquals(1, target.publicField);
    }

    @Test
    public void testSendsForFieldUnderDomainObject() throws Exception {
        Message check = fixture.check("obj publicfield");
        assertEquals(0, check.send());

        Message send = fixture.send("obj publicfield");
        send.send("1");
        assertEquals(1, target.obj.publicField);
    }

    @Test
    public void testSendsForMethodUnderDomainObject() throws Exception {
        Message check = fixture.check("sut obj");
        Object old = check.send();

        Message send = fixture.send("sut obj");
        send.send("toto");

        assertNotSame(old, target.sut.sutQuery());
    }

    @Test
    public void testSendsForBeanPropertyInFixture() throws Exception {
        Message check = fixture.check("property");
        assertEquals(0, check.send());

        Message send = fixture.send("property");
        send.send("1");
        assertEquals(1, target.getProperty());
    }

    @Test
    public void testSendsForBeanPropertyUnderDomainObjectInFixture() throws Exception {
        Message check = fixture.check("method returning object property");
        assertEquals(0, check.send());

        Message send = fixture.send("method returning object property");
        send.send("1");
        assertEquals(1, target.methodReturningObject().getProperty());
    }

    @Test
    public void testSendsForSetterObjectUnderOneDepthDomainObjectInFixture() throws Exception {
        Object old = target.methodReturningObject();

        Message send = fixture.send("returning object");
        assertNotNull(send);

        send.send("toto");

        Message check = fixture.check("returning object");

        Object actual = check.send();

        assertNotSame(old, actual);
    }

    @Test
    public void testSendsForBeanPropertyUnderTwoDepthDomainObjectInFixture() throws Exception {
        Message check = fixture.check("sut sut query property");
        assertEquals(0, check.send());

        Message send = fixture.send("sut sut query property");
        send.send("1");
        assertEquals(1, target.sut.sutQuery().getProperty());
    }

    @Test(expected = NoSuchMessageException.class)
    public void testSendsForVoidMethodThatComplain() throws Exception {
        fixture.check("obj void method toto");
    }

    @Test(expected = NoSuchMessageException.class)
    public void testSendsForUnknowMethodThatComplain() throws Exception {
        fixture.send("unknow");
    }

    @Test
    public void testGetterMethodCallForUnknowMethod() {
        try {
            fixture.check("unknow");
            fail("No such method expected");
        } catch (Exception e) {
            assertTrue(true);
        }

    }

    @Test
    public void testGetterMethodCallForAnExistingMethod() throws Exception {
        target.setProperty(1);

        Message received = fixture.check("property");
        assertNotNull(received);

        assertEquals(1, received.send());
    }

    @Test
    public void testGetterMethodCallOnSUTMethod() throws Exception {
        target.sut.setName("MyName");
        Message received = fixture.check("name");
        assertNotNull(received);

        assertEquals("MyName", received.send());
    }

    @Test
    public void testLooksForCommandOgnlSutExpressionIfNotFoundOnFixtureAndSut() throws Exception {
        Message send = fixture.send("sutQuery publicField");
        send.send("1");
        assertEquals(1, target.getSystemUnderTest().sutQuery().publicField);
    }

    public class Target {
        public int publicField;

        public Sut sut = new Sut();
        public Obj obj = new Obj();

        public void setProperty(int value) {
            publicField = value;
        }

        public int getProperty() {
            return publicField;
        }

        public Obj methodReturningObject() {
            return obj;
        }

        public Obj getReturningObject() {
            return obj;
        }

        public void setReturningObject(Obj obj) {
            this.obj = obj;
        }

        @SystemUnderTest
        public Sut getSystemUnderTest() {
            return sut;
        }
    }

    public class Sut {
        public Obj obj = new Obj();
        private String name;

        public Obj sutQuery() {
            return obj;
        }

        public Obj getObjX() {
            return obj;
        }

        public void setObj(Obj obj) {
            this.obj = obj;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Obj {
        public int publicField;
        private Object obj = new Object();

        public void setProperty(int value) {
            publicField = value;
        }

        public int getProperty() {
            return publicField;
        }

        public Object methodReturningObject() {
            return obj;
        }

        public void voidMethod() {
            // No implementation needed.
        }

        public static Obj parse(String value) {
            return new Obj();
        }
    }
}
