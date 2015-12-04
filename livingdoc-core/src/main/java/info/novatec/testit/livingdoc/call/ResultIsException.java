package info.novatec.testit.livingdoc.call;

public class ResultIsException implements ResultMatcher {
    @Override
    public boolean matches(Result result) {
        return result.isException();
    }
}
