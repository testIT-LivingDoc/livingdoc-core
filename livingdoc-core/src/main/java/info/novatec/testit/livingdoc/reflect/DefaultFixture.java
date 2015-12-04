package info.novatec.testit.livingdoc.reflect;

import info.novatec.testit.livingdoc.ogn.ObjectGraphNavigationFixture;


public class DefaultFixture implements Fixture {
    private final Fixture delegate;

    public DefaultFixture(Object target) {
        delegate = new ObjectGraphNavigationFixture(target);
    }

    @Override
    public Fixture fixtureFor(Object target) {
        return new DefaultFixture(target);
    }

    @Override
    public Object getTarget() {
        return delegate.getTarget();
    }

    @Override
    public boolean canSend(String message) {
        return delegate.canSend(message);
    }

    @Override
    public boolean canCheck(String message) {
        return delegate.canCheck(message);
    }

    @Override
    public Message check(String message) throws NoSuchMessageException {
        return delegate.check(message);
    }

    @Override
    public Message send(String message) throws NoSuchMessageException {
        return delegate.send(message);
    }
}
