package info.novatec.testit.livingdoc.call;

public class ResultIsRight implements ResultMatcher {
    @Override
    public boolean matches(Result result) {
        return result.isRight();
    }
}
