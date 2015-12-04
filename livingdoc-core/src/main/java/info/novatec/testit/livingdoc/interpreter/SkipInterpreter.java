package info.novatec.testit.livingdoc.interpreter;

import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY_WITH;
import static info.novatec.testit.livingdoc.util.LoggerConstants.EXIT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.Statistics;


public class SkipInterpreter extends AbstractInterpreter {
    private static final Logger LOG = LoggerFactory.getLogger(SkipInterpreter.class);

    private final Statistics stats;

    public SkipInterpreter() {
        this(new Statistics());
    }

    public SkipInterpreter(Statistics stats) {
        this.stats = stats;
    }

    @Override
    public void interpret(Specification specification) {
        LOG.debug(ENTRY_WITH, specification.toString());
        specification.nextExample();
        specification.exampleDone(stats);
        LOG.debug(EXIT);
    }
}
