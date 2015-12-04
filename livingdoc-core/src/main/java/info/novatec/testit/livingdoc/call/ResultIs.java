package info.novatec.testit.livingdoc.call;

public final class ResultIs {
    public static ResultMatcher wrong() {
        return new ResultIsWrong();
    }

    public static ResultMatcher not(ResultMatcher condition) {
        return new ResultIsNot(condition);
    }

    public static ResultMatcher exception() {
        return new ResultIsException();
    }

    public static ResultMatcher equalTo(Object value) {
        return new ActualEquals(value);
    }

    public static ResultMatcher right() {
        return new ResultIsRight();
    }

    private ResultIs() {
    }
}
