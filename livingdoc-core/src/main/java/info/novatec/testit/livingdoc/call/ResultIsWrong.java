package info.novatec.testit.livingdoc.call;

public class ResultIsWrong implements ResultMatcher {
    @Override
    public boolean matches(Result result) {
        return result.isWrong();
    }
}
