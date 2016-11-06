package info.novatec.testit.livingdoc.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;


public class PlainOldFixtureTest {
    private Fixture fixture;
    private Target target;

    @Before
    public void setUp() throws Exception {
        target = new Target();
        fixture = new PlainOldFixture(target);
    }

    @Test
    public void testCheckMessagesIncludeMethods() throws Exception {
        target.field = "value";
        Message check = fixture.check("methodReturningField");
        assertEquals(target.field, check.send());
    }

    @Test
    public void testCheckMessageNamesAreNotCaseSensitive() throws Exception {
        target.field = "value";
        Message check = fixture.check("methodReturningField".toUpperCase());
        assertEquals(target.field, check.send());
    }

    @Test
    public void testCheckMessageNamesCanBeHumanized() throws Exception {
        target.field = "value";
        Message check = fixture.check("method returning field");
        assertEquals(target.field, check.send());
    }

    @Test
    public void testCheckMessagesIncludeGetters() throws Exception {
        target.field = "value";
        Message check = fixture.check("getter");
        assertEquals(target.field, check.send());
    }

    @Test
    public void testCheckMessagesIncludeFields() throws Exception {
        target.field = "value";
        Message check = fixture.check("field");
        assertEquals(target.field, check.send());
    }

    @Test
    public void testSendMessagesIncludeMethods() throws Exception {
        target.field = "value";
        Message send = fixture.send("methodReturningField");
        assertEquals(target.field, send.send());
    }

    @Test
    public void testSendMessageNamesAreNotCaseSensitive() throws Exception {
        target.field = "value";
        Message send = fixture.send("methodReturningField".toUpperCase());
        assertEquals(target.field, send.send());
    }

    @Test
    public void testSendMessageNamesCanBeHumanized() throws Exception {
        target.field = "value";
        Message send = fixture.send("method returning field");
        assertEquals(target.field, send.send());
    }

    @Test
    public void testSendMessageIncludeSetters() throws Exception {
        Message send = fixture.send("setter");
        send.send("value");
        assertEquals("value", target.field);
    }

    @Test
    public void testSendMessagesIncludeFields() throws Exception {
        Message send = fixture.send("field");
        send.send("value");
        assertEquals("value", target.field);
    }

    @Test(expected = NoSuchMessageException.class)
    public void testComplainsIfCheckMessageIsNotUnderstood() throws Exception {
        fixture.check("no such message");
    }

    @Test(expected = NoSuchMessageException.class)
    public void testComplainsIfSendMessageIsNotUnderstood() throws Exception {
        fixture.send("no such message");
    }

    @Test
    public void testLooksForMessageOnSystemUnderTestIfNotFoundOnFixture() throws Exception {
        Message check = fixture.check("name");
        assertEquals("sut", check.send());
        Message send = fixture.send("name");
        assertEquals("sut", send.send());
    }

    @Test
    public void testLooksForAGetSystemUnderTestMethodIfAnnotationNotFound() throws Exception {
        Fixture plainOldFixture = new PlainOldFixture(new WithoutSystemUnderTestAnnotation());
        Message check = plainOldFixture.check("name");
        assertEquals("sut", check.send());
    }

    @Test
    public void testGetterMethodCallANoParametersMethod() {
        try {
            target.field = "something";
            Message getter = fixture.check("methodReturningField");
            assertEquals(target.field, getter.send());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCanCheckTheProperlyMethodDependingOnArity() throws Exception {
        Message message = fixture.check("functionWithDifferentArities");

        assertNotNull(message);

        assertEquals(new Integer(0), message.send());

        assertEquals(new Integer(1), message.send(new String[] { "1" }));

        assertEquals(new Integer(2), message.send(new String[] { "1", "2" }));

        try {
            message.send(new String[] { "1", "2", "3" });
            fail("Must throw an exception");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCanSendTheProperlyMethodDependingOnArity() throws Exception {
        Message message = fixture.send("AttributeWithDifferentArities");
        assertNotNull(message);

        message.send(new String[] { "1" });

        assertEquals(1, target.attributeWithDifferentArities);

        try {
            message.send();
            assertTrue(true);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public class Target {
        public String field;

        public Object sut = new Sut();

        public int attributeWithDifferentArities = 0;

        public void attributeWithDifferentArities() {
            // No implementation needed.
        }

        public int functionWithDifferentArities() {
            return 0;
        }

        public int functionWithDifferentArities(int aParam) {
            return 1;
        }

        public int functionWithDifferentArities(int aParam, int anotherParam) {
            return 2;
        }

        public void setSetter(String s) {
            field = s;
        }

        public String getGetter() {
            return field;
        }

        public String methodReturningField() {
            return field;
        }

        public void voidMethod() {
            // No implementation needed.
        }

        @SystemUnderTest
        public Object systemUnderTest() {
            return sut;
        }

    }

    public static class WithoutSystemUnderTestAnnotation {

        public Object sut = new Sut();

        public Object getSystemUnderTest() {
            return sut;
        }
    }

    public static class Sut {
        public String name() {
            return "sut";
        }
    }

}
