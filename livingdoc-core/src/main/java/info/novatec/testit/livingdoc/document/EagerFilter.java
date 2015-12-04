package info.novatec.testit.livingdoc.document;

import info.novatec.testit.livingdoc.Example;


public class EagerFilter implements ExampleFilter {
    private final ExampleFilter filter;

    public EagerFilter(ExampleFilter filter) {
        this.filter = filter;
    }

    @Override
    public boolean canFilter(Example example) {
        return filter.canFilter(example);
    }

    @Override
    public Example filter(Example example) {
        Example next = example;
        while (next != null && canFilter(next)) {
            next = filter.filter(next);
        }
        return next;
    }
}
