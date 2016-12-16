package info.novatec.testit.livingdoc.ognl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.Message;
import info.novatec.testit.livingdoc.reflect.NoSuchMessageException;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.reflect.SystemUnderTest;


public class OgnlFixtureTest {
    private Fixture fixture;
    private Target target;

    @Before
    public void setUp() throws Exception {
        target = new Target();
        fixture = new OgnlFixture(new PlainOldFixture(target));
    }

    @Test
    public void testLooksForCommandOgnlFixtureExpressionIfNotFoundOnFixtureAndSut() throws Exception {
        Message send = fixture.send("methodReturningObject().publicField");
        send.send("1");
        assertEquals(1, target.methodReturningObject().publicField);
    }

    @Test
    public void testLooksForCommandOgnlSutExpressionIfNotFoundOnFixtureAndSut() throws Exception {
        Message send = fixture.send("sutQuery().publicField");
        send.send("1");
        assertEquals(1, target.getSystemUnderTest().sutQuery().publicField);
    }

    @Test(expected = UnresolvableExpressionException.class)
    public void testCommandExpressionThatCantBeResolveShouldComplain() throws Exception {
        Message send = fixture.send("unexistentFixtureAndSutMethod().field");
        send.send("dummy");
    }

    @Test(expected = NoSuchMessageException.class)
    public void testCommandExpressionThatCauseAParseErrorComplain() throws Exception {
        fixture.send("no such action");
    }

    @Test
    public void testLooksForQueryOgnlFixtureExpressionIfNotFoundOnFixtureAndSut() throws Exception {
        Message check = fixture.check("methodReturningObject().publicField");
        assertEquals(0, check.send());

        check = fixture.check("methodReturningObject().methodReturningValue()");
        assertEquals(true, check.send());

        check = fixture.check("methodReturningObject().methodReturningObject()");
        assertEquals(target.methodReturningObject().methodReturningObject(), check.send());
    }

    @Test
    public void testLooksForQueryOgnlSutExpressionIfNotFoundOnFixtureAndSut() throws Exception {
        Message check = fixture.check("sutQuery().publicField");
        assertEquals(0, check.send());
    }

    @Test(expected = UnresolvableExpressionException.class)
    public void testQueryExpressionThatCantBeResolveShouldComplain() throws Exception {
        Message check = fixture.check("unexistentFixtureAndSutMethod().field");
        check.send();
    }

    @Test(expected = NoSuchMessageException.class)
    public void testQueryExpressionThatCauseAParseErrorComplain() throws Exception {
        fixture.check("no such action");
    }

    public class Target {
        public int publicField;

        public Sut sut = new OgnlFixtureTest.Sut();
        public Obj obj = new OgnlFixtureTest.Obj();

        public void setProperty(int value) {
            publicField = value;
        }

        public int getProperty() {
            return publicField;
        }

        public void voidMethod() {
            // No implementation needed.
        }

        public boolean methodReturningValue() {
            return true;
        }

        public Obj methodReturningObject() {
            return obj;
        }

        @SystemUnderTest
        public Sut getSystemUnderTest() {
            return sut;
        }

        public void methodWithArityOverloaded() {
            // No implementation needed.
        }

        public void methodWithArityOverloaded(String s1) {
            // No implementation needed.
        }

        public void methodWithArityOverloaded(String s1, String s2) {
            // No implementation needed.
        }

        public boolean methodReturningValueWithArityOverloaded() {
            return true;
        }

        public boolean methodReturningValueWithArityOverloaded(String s1) {
            return true;
        }

        public boolean methodReturningValueWithArityOverloaded(String s1, String s2) {
            return true;
        }
    }

    public class Sut {
        public Obj obj = new Obj();

        public boolean action() {
            return true;
        }

        public OgnlFixtureTest.Obj sutQuery() {
            return obj;
        }
    }

    public class Obj {
        public int publicField;
        private Object obj = new Object();

        public void setProperty(int value) {
            publicField = value;
        }

        public int getProperty() {
            return publicField;
        }

        public void voidMethod() {
            // No implementation needed.
        }

        public boolean methodReturningValue() {
            return true;
        }

        public Object methodReturningObject() {
            return obj;
        }
    }
}
