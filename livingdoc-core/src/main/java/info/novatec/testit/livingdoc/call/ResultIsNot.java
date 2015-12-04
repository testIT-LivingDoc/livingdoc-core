package info.novatec.testit.livingdoc.call;

public class ResultIsNot implements ResultMatcher {
    private final ResultMatcher condition;

    public ResultIsNot(ResultMatcher condition) {
        this.condition = condition;
    }

    @Override
    public boolean matches(Result result) {
        return ! condition.matches(result);
    }
}
