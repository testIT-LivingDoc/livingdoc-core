package info.novatec.testit.livingdoc.call;

import info.novatec.testit.livingdoc.Statistics;


public class CompileStatistics implements Stub {
    private final Statistics stats;

    public CompileStatistics(Statistics stats) {
        this.stats = stats;
    }

    @Override
    public void call(Result result) {
        if (result.isRight()) {
            stats.right();
        }
        if (result.isWrong()) {
            stats.wrong();
        }
        if (result.isException()) {
            stats.exception();
        }
        if (result.isIgnored()) {
            stats.ignored();
        }
    }
}
