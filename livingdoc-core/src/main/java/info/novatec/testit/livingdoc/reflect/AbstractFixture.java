package info.novatec.testit.livingdoc.reflect;

import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;
import static info.novatec.testit.livingdoc.util.NameUtils.toJavaIdentifierForm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.util.Introspector;


public abstract class AbstractFixture implements Fixture {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractFixture.class);

    protected final Object target;

    public AbstractFixture(Object target) {
        this.target = target;
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public boolean canSend(String message) {
        return getSendMessage(message) != null;
    }

    @Override
    public boolean canCheck(String message) {
        return getCheckMessage(message) != null;
    }

    @Override
    public Message check(String message) throws NoSuchMessageException {
        Message check = getCheckMessage(message);
        if (check == null) {
            throw new NoSuchMessageException(message);
        }

        return check;
    }

    @Override
    public Message send(String message) throws NoSuchMessageException {
        Message send = getSendMessage(message);
        if (send == null) {
            throw new NoSuchMessageException(message);
        }

        return send;
    }

    protected abstract Message getCheckMessage(String name);

    protected abstract Message getSendMessage(String name);

    protected Method getSetter(Class< ? extends Object> type, String name) {
        return introspector(type).getSetter(toJavaIdentifierForm(name));
    }

    protected Field getField(Class< ? extends Object> type, String name) {
        return introspector(type).getField(toJavaIdentifierForm(name));
    }

    protected List<Method> getMethods(Class< ? extends Object> type, String name) {
        return introspector(type).getMethods(toJavaIdentifierForm(name));
    }

    protected Method getGetter(Class< ? extends Object> type, String name) {
        return introspector(type).getGetter(toJavaIdentifierForm(name));
    }

    protected Introspector introspector(Class< ? extends Object> type) {
        return Introspector.ignoringCase(type);
    }

    protected Object getSystemUnderTest() {
        for (Method method : target.getClass().getMethods()) {
            if (method.isAnnotationPresent(SystemUnderTest.class)) {
                return invoke(method);
            }
        }

        Method m = getGetter(target.getClass(), "systemUnderTest");
        return m != null ? invoke(m) : null;
    }

    protected Object invoke(Method method) {
        try {
            return method.invoke(target);
        } catch (Exception e) {
            LOG.error(LOG_ERROR, e);
            return null;
        }
    }
}
