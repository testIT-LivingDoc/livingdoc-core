package info.novatec.testit.livingdoc.call;

import info.novatec.testit.livingdoc.Statistics;


public final class Compile {
    public static Stub statistics(Statistics stats) {
        return new CompileStatistics(stats);
    }

    private Compile() {
    }
}
