package info.novatec.testit.livingdoc.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class PlainOldFixture extends AbstractFixture {
    public PlainOldFixture(Object target) {
        super(target);
    }

    @Override
    public Fixture fixtureFor(Object paramTarget) {
        return new PlainOldFixture(paramTarget);
    }

    @Override
    protected Message getCheckMessage(String name) {
        final Class< ? extends Object> type = target.getClass();

        InvocationMessage invocationMessage = getInvocations(name);

        Method getterMethod = getGetter(type, name);

        if (getterMethod != null) {
            invocationMessage.addMessage(new StaticInvocation(target, getterMethod));
        }

        Field field = getField(type, name);
        if (field != null) {
            invocationMessage.addMessage(new FieldReader(target, field));
        }

        if ( ! invocationMessage.isEmpty()) {
            return invocationMessage;
        }

        if (getSystemUnderTest() == null) {
            return null;
        }

        PlainOldFixture fixture = new PlainOldFixture(getSystemUnderTest());
        return fixture.getCheckMessage(name);
    }

    @Override
    protected Message getSendMessage(String name) {
        final Class< ? extends Object> type = target.getClass();

        InvocationMessage invocationMessage = getInvocations(name);

        Method setterMethod = getSetter(type, name);
        if (setterMethod != null) {
            invocationMessage.addMessage(new StaticInvocation(target, setterMethod));
        }

        Field field = getField(type, name);
        if (field != null) {
            invocationMessage.addMessage(new FieldWriter(target, field));
        }

        if ( ! invocationMessage.isEmpty()) {
            return invocationMessage;
        }

        if (getSystemUnderTest() == null) {
            return null;
        }

        PlainOldFixture fixture = new PlainOldFixture(getSystemUnderTest());
        return fixture.getSendMessage(name);
    }

    private InvocationMessage getInvocations(String name) {
        InvocationMessage invocationMessage = new InvocationMessage();

        for (Method method : getMethods(target.getClass(), name)) {
            invocationMessage.addMessage(new StaticInvocation(target, method));
        }
        return invocationMessage;
    }
}
