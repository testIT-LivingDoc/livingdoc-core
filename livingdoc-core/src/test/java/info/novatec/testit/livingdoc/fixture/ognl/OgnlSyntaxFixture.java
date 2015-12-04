package info.novatec.testit.livingdoc.fixture.ognl;

import java.lang.reflect.InvocationTargetException;

import info.novatec.testit.livingdoc.Call;
import info.novatec.testit.livingdoc.ognl.OgnlFixture;
import info.novatec.testit.livingdoc.ognl.OgnlGetter;
import info.novatec.testit.livingdoc.ognl.OgnlSetter;
import info.novatec.testit.livingdoc.reflect.DefaultFixture;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.Message;
import info.novatec.testit.livingdoc.reflect.NoSuchMessageException;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


@FixtureClass
public class OgnlSyntaxFixture {
    private Message msg;

    private Fixture target;

    public OgnlSyntaxFixture() {
        this.target = new OgnlFixture(new DefaultFixture(new ActionExampleResolution()));
    }

    public void check(String action) throws NoSuchMessageException {
        msg = target.check(action);
    }

    public String result() throws IllegalArgumentException, InvocationTargetException, IllegalAccessException {
        Call call = new Call(msg);
        return call.execute(new String[0]).toString();
    }

    public boolean isUsingOGNL() {
        return msg instanceof OgnlSetter || msg instanceof OgnlGetter;
    }

}
