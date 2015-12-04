package info.novatec.testit.livingdoc.document;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Statistics;


public class FakeSpecification extends AbstractSpecification {
    public Statistics stats = new Statistics();

    public FakeSpecification(Example start) {
        setStart(start);
    }

    @Override
    protected Example peek() {
        return cursor.nextSibling();
    }

    @Override
    public void exampleDone(Statistics statistics) {
        stats.tally(statistics);
    }

    public Statistics stats() {
        return stats;
    }
}
