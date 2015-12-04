package info.novatec.testit.livingdoc;

public interface Specification extends ExecutionContext {
    Example nextExample();

    boolean hasMoreExamples();

    void exampleDone(Statistics statistics);
}
