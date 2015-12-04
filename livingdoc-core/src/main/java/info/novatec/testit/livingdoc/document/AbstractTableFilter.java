package info.novatec.testit.livingdoc.document;

import static info.novatec.testit.livingdoc.util.ExampleUtil.contentOf;

import info.novatec.testit.livingdoc.Example;


public abstract class AbstractTableFilter implements ExampleFilter {
    private final String[] keywords;

    protected AbstractTableFilter(String... keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean canFilter(Example example) {
        for (String keyword : keywords) {
            if (keyword.equalsIgnoreCase(contentOf(example.at(0, 0, 0)))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Example filter(Example example) {
        if ( ! canFilter(example)) {
            return example;
        }
        return doFilter(example);
    }

    protected abstract Example doFilter(Example example);
}
