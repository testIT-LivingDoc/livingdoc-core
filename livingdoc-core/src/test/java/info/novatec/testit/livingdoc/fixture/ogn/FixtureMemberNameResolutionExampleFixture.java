package info.novatec.testit.livingdoc.fixture.ogn;

import java.lang.reflect.InvocationTargetException;

import info.novatec.testit.livingdoc.Call;
import info.novatec.testit.livingdoc.fixture.seeds.action.ActionExampleResolution;
import info.novatec.testit.livingdoc.reflect.DefaultFixture;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.Message;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


@FixtureClass("Fixture Member Name Resolution Example Ogn")
public class FixtureMemberNameResolutionExampleFixture {
    public String call;
    private Fixture target;

    public FixtureMemberNameResolutionExampleFixture() {
        this.target = new DefaultFixture(new ActionExampleResolution());
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
