package info.novatec.testit.livingdoc.fixture.ognl;

import java.lang.reflect.InvocationTargetException;

import info.novatec.testit.livingdoc.Call;
import info.novatec.testit.livingdoc.ognl.OgnlFixture;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.Message;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


@FixtureClass
public class FixtureMemberNameResolutionExampleFixture {
    public String call;
    private Fixture target;

    public FixtureMemberNameResolutionExampleFixture() {
        this.target = new OgnlFixture(new PlainOldFixture(new ActionExampleResolution()));
    }

    public String returnString() throws IllegalArgumentException, InvocationTargetException, IllegalAccessException {
        Message msg = target.check(call);
        Call callExecute = new Call(msg);
        String[] args = args(msg);
        return callExecute.execute(args).toString();
    }

    private String[] args(Message msg) {

        int arity = msg.getArity();
        String[] args = new String[arity];

        for (int i = 0; i < arity; i ++ ) {
            args[i] = "1";
        }

        return args;

    }
}
