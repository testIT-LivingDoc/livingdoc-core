package info.novatec.testit.livingdoc.call;

public class ResultHandler implements StubSyntax {
    private final Stub stub;
    private ResultMatcher matcher;

    public ResultHandler(Stub stub) {
        this.stub = stub;
        this.matcher = new AnyResult();
    }

    @Override
    public void when(ResultMatcher resultMatcher) {
        this.matcher = resultMatcher;
    }

    public void handle(Result result) {
        if (matcher.matches(result)) {
            stub.call(result);
        }
    }
}
