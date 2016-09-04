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
import info.novatec.testit.livingdoc.call.ResultIs;
import info.novatec.testit.livingdoc.interpreter.flow.AbstractRow;
import info.novatec.testit.livingdoc.interpreter.flow.Action;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.util.CollectionUtil;
import info.novatec.testit.livingdoc.util.ExampleUtil;


public class CheckRow extends AbstractRow {
    private static final Logger LOG = LoggerFactory.getLogger(CheckRow.class);

    public static boolean matches(String keyword) {
        return "check".equalsIgnoreCase(keyword);
    }

    public CheckRow(Fixture fixture) {
        super(fixture);
    }

    @Override
    public void interpret(Specification table) {
        Example row = table.nextExample();

        Example expectedCell = row.firstChild().lastSibling();
        Action action = Action.parse(actionCells(row));

        try {
            Call call = action.checkAgainst(fixture);
            call.expect(expectedCell.getContent());
            call.will(Annotate.right(expectedCell)).when(ResultIs.right());
            call.will(Annotate.wrongWithDetails(expectedCell)).when(ResultIs.wrong());
            call.will(Annotate.exception(CollectionUtil.first(keywordCells(row)))).when(ResultIs.exception());
            call.will(tallyStatistics(table));
            call.execute();
        } catch (Exception e) {
            LOG.error(LOG_ERROR, e);
            reportException(table);
            CollectionUtil.first(keywordCells(row)).annotate(Annotations.exception(e));
        }
    }

    private List<Example> keywordCells(Example row) {
        return CollectionUtil.odd(row.firstChild());
    }

    @Override
    public List<Example> actionCells(Example row) {
        List<Example> actionCells = ExampleUtil.asList(row.at(0, 1));
        CollectionUtil.removeLast(actionCells);
        return actionCells;
    }
}
