package info.novatec.testit.livingdoc.ognl;

import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.Message;
import info.novatec.testit.livingdoc.reflect.NoSuchMessageException;
import info.novatec.testit.livingdoc.reflect.SystemUnderTest;


public class OgnlFixture implements Fixture {
    private static final Logger LOG = LoggerFactory.getLogger(OgnlFixture.class);

    private final Fixture decorated;

    public OgnlFixture(Fixture decorated) {
        this.decorated = decorated;
    }

    @Override
    public Object getTarget() {
        return decorated.getTarget();
    }

    @Override
    public Fixture fixtureFor(Object target) {
        return new OgnlFixture(decorated.fixtureFor(target));
    }

    @Override
    public boolean canSend(String message) {
        return decorated.canSend(message) || getSendMessage(message) != null;
    }

    @Override
    public boolean canCheck(String message) {
        return decorated.canCheck(message) || getCheckMessage(message) != null;
    }

    @Override
    public Message check(String message) throws NoSuchMessageException {
        if (decorated.canCheck(message)) {
            return decorated.check(message);
        }

        Message check = getCheckMessage(message);
        if (check == null) {
            throw new NoSuchMessageException(message);
        }

        return check;
    }

    @Override
    public Message send(String message) throws NoSuchMessageException {
        if (decorated.canSend(message)) {
            return decorated.send(message);
        }

        Message send = getSendMessage(message);
        if (send == null) {
            throw new NoSuchMessageException(message);
        }

        return send;
    }

    private Object[] getTargets() {
        return getSystemUnderTest() != null ? new Object[] { getTarget(), getSystemUnderTest() } : new Object[] {
                getTarget() };
    }

    private Message getCheckMessage(String message) {
        if ( ! OgnlExpression.isGetter(message)) {
            return null;
        }

        OgnlExpression ognlExpression = OgnlExpression.onUnresolvedExpression(message, getTargets());
        return new OgnlGetter(ognlExpression);
    }

    private Message getSendMessage(String key) {
        if ( ! OgnlExpression.isSetter(key)) {
            return null;
        }
        OgnlExpression ognlExpression = OgnlExpression.onUnresolvedExpression(key, getTargets());

        return new OgnlSetter(ognlExpression);
    }

    private Object getSystemUnderTest() {
        for (Method method : getTarget().getClass().getMethods()) {
            if (method.isAnnotationPresent(SystemUnderTest.class)) {
                return invoke(method);
            }
        }
        return null;
    }

    private Object invoke(Method method) {
        try {
            return method.invoke(getTarget());
        } catch (InvocationTargetException e) {
            LOG.error(LOG_ERROR, e);
        } catch (IllegalAccessException e) {
            LOG.error(LOG_ERROR, e);
        } catch (IllegalArgumentException e) {
            LOG.error(LOG_ERROR, e);
        }
        return null;
    }
}
