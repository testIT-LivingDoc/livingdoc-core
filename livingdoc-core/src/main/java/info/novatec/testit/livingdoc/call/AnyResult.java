package info.novatec.testit.livingdoc.call;

public class AnyResult implements ResultMatcher {
    @Override
    public boolean matches(Result result) {
        return true;
    }
}
