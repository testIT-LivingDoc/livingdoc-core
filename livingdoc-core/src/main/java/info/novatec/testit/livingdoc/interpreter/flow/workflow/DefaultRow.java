package info.novatec.testit.livingdoc.interpreter.flow.workflow;

import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.Call;
import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Specification;
import info.novatec.testit.livingdoc.annotation.Annotations;
import info.novatec.testit.livingdoc.call.Annotate;
import info.novatec.testit.livingdoc.call.Do;
import info.novatec.testit.livingdoc.call.ResultIs;
import info.novatec.testit.livingdoc.interpreter.flow.AbstractRow;
import info.novatec.testit.livingdoc.interpreter.flow.Action;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.util.CollectionUtil;
import info.novatec.testit.livingdoc.util.ExampleUtil;
import info.novatec.testit.livingdoc.util.Group;


public class DefaultRow extends AbstractRow {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultRow.class);

    public DefaultRow(Fixture fixture) {
        super(fixture);
    }

    @Override
    public void interpret(Specification table) {
        Example row = table.nextExample();
        Action action = Action.parse(actionCells(row));
        try {
            Call call = action.checkAgainst(fixture);
            call.will(Do.both(Annotate.right(Group.composedOf(keywordCells(row)))).and(countRowOf(table).right())).when(
                ResultIs.equalTo(true));
            call.will(Do.both(Annotate.wrong(Group.composedOf(keywordCells(row)))).and(countRowOf(table).wrong())).when(
                ResultIs.equalTo(false));
            call.will(Do.both(Annotate.exception(CollectionUtil.first(keywordCells(row)))).and(countRowOf(table)
                .exception())).when(ResultIs.exception());
            call.execute();
        } catch (Exception e) {
            LOG.error(LOG_ERROR, e);
            CollectionUtil.first(keywordCells(row)).annotate(Annotations.exception(e));
            reportException(table);
        }
    }

    private List<Example> keywordCells(Example row) {
        return CollectionUtil.even(row.firstChild());
    }

    @Override
    public List<Example> actionCells(Example row) {
        return ExampleUtil.asList(row.firstChild());
    }
}
