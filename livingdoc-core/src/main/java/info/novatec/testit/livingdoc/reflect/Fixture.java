package info.novatec.testit.livingdoc.reflect;

public interface Fixture {
    boolean canSend(String message);

    boolean canCheck(String message);

    Message check(String message) throws NoSuchMessageException;

    Message send(String message) throws NoSuchMessageException;

    Object getTarget();

    Fixture fixtureFor(Object target);
}
