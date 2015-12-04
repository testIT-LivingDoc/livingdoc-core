package info.novatec.testit.livingdoc.call;

public class ActualEquals implements ResultMatcher {
    private final Object value;

    public ActualEquals(Object value) {
        this.value = value;
    }

    @Override
    public boolean matches(Result result) {
        return value.equals(result.getActual());
    }
}
