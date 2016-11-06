package info.novatec.testit.livingdoc.interpreter;

import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY_WITH;
import static info.novatec.testit.livingdoc.util.LoggerConstants.EXIT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Interpreter;
import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.annotation.IgnoredAnnotation;
import info.novatec.testit.livingdoc.annotation.RightAnnotation;
import info.novatec.testit.livingdoc.annotation.WrongAnnotation;
import info.novatec.testit.livingdoc.systemunderdevelopment.SystemUnderDevelopment;


public class MyOwnInterpreter implements Interpreter  {
    private static final Logger LOG = LoggerFactory.getLogger(MyOwnInterpreter.class);

    public MyOwnInterpreter(SystemUnderDevelopment sud) {
        // No implementation needed.
    }

    @Override
    public void interpret(Specification specification) {
        LOG.debug(ENTRY_WITH, specification.toString());
        Statistics stats = new Statistics();
        Example table = specification.nextExample();

        for (Example row = table.at(0, 1); row != null; row = row.nextSibling()) {
            doRow(row);
        }

        specification.exampleDone(stats);
        LOG.debug(EXIT);
    }

    private void doRow(Example row) {
        LOG.trace(ENTRY_WITH, row.toString());
        if ("right".equals(row.firstChild().at(0).getContent())) {
            row.firstChild().at(1).annotate(new RightAnnotation());
        }

        if ("wrong".equals(row.firstChild().at(0).getContent())) {
            row.firstChild().at(1).annotate(new WrongAnnotation());
        }

        if ("ignore".equals(row.firstChild().at(0).getContent())) {
            row.firstChild().at(1).annotate(new IgnoredAnnotation(""));
        }
        LOG.trace(EXIT);
    }

}
