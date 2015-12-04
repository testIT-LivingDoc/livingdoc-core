package info.novatec.testit.livingdoc.fixture.seeds.action;

import java.lang.reflect.InvocationTargetException;

import info.novatec.testit.livingdoc.reflect.Message;
import info.novatec.testit.livingdoc.reflect.NoSuchMessageException;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.reflect.SystemUnderDevelopmentException;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


@FixtureClass
public class ActionAccessResolutionFixture {
    public String access;
    private PlainOldFixture target;

    public ActionAccessResolutionFixture() {
        this.target = new PlainOldFixture(new ActionExampleResolution());
    }

    public boolean canSetAValueUsing() throws InvocationTargetException, IllegalAccessException,
        SystemUnderDevelopmentException {
        try {
            target.send(access).send("1");
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        } catch (NoSuchMessageException e) {
            return false;
        }
    }

    public boolean canGetAValueUsing() throws IllegalArgumentException, InvocationTargetException, IllegalAccessException,
        SystemUnderDevelopmentException {
        try {
            Message msg = target.check(access);
            return msg.send(args(msg)) != null;
        } catch (NoSuchMessageException e) {
            return false;
        }
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
