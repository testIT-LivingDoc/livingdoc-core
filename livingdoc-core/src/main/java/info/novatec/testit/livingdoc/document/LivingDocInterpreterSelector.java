package info.novatec.testit.livingdoc.document;

import static info.novatec.testit.livingdoc.util.CollectionUtil.*;
import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Interpreter;
import info.novatec.testit.livingdoc.LivingDoc;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.annotation.Annotations;
import info.novatec.testit.livingdoc.interpreter.SkipInterpreter;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.systemunderdevelopment.SystemUnderDevelopment;
import info.novatec.testit.livingdoc.util.CollectionUtil;
import info.novatec.testit.livingdoc.util.ExampleUtil;


/**
 * This class is responsible for selecting an interpreter from the first row of
 * a table.
 */
public class LivingDocInterpreterSelector implements InterpreterSelector {

    private static final Logger LOG = LoggerFactory.getLogger(LivingDocInterpreterSelector.class);

    protected final SystemUnderDevelopment systemUnderDevelopment;

    public LivingDocInterpreterSelector(SystemUnderDevelopment systemUnderDevelopment) {
        this.systemUnderDevelopment = systemUnderDevelopment;
    }

    @Override
    public Interpreter selectInterpreter(Example table) {
        Example cells = table.at(0, 0, 0);
        if (cells == null) {
            return new SkipInterpreter();
        }

        String interpreterName = cells.at(0).getContent();
        String[] fixtureAndParameters = fixtureAndParams(cells.at(1));
        try {
            Object[] args = isEmpty(fixtureAndParameters) ? toArray(systemUnderDevelopment) : toArray(
                selectFixture(fixtureAndParameters));
            return LivingDoc.getInterpreter(interpreterName, args);
        } catch (Throwable t) {
            LOG.error(LOG_ERROR, t);
            cells.at(0).annotate(Annotations.exception(t));
            return new SkipInterpreter(new Statistics(0, 0, 1, 0));
        }
    }

    private String[] fixtureAndParams(Example cell) {
        List<Example> list = CollectionUtil.even(ExampleUtil.asList(cell));
        return ExampleUtil.content(list);
    }

    private Fixture selectFixture(String[] fixtureAndParameters) throws Throwable {
        List<String> values = toList(fixtureAndParameters);
        String name = CollectionUtil.shift(values);
        return systemUnderDevelopment.getFixture(name, values.toArray(new String[values.size()]));
    }
}
