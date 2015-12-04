package info.novatec.testit.livingdoc.document;

import info.novatec.testit.livingdoc.Example;


public interface ExampleFilter {
    boolean canFilter(Example example);

    Example filter(Example example);
}
