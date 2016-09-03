package info.novatec.testit.livingdoc.interpreter.flow.action;

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
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.util.ExampleUtil;


@Deprecated
public class ActionRow extends AbstractRow {
    private static final Logger LOG = LoggerFactory.getLogger(ActionRow.class);

    protected ActionRow(Fixture fixture) {
        super(fixture);
    }

    @Override
    public List<Example> actionCells(Example row) {
        return ExampleUtil.asList(row.firstChild());
    }

    @Override
    public void interpret(Specification spec) {
        Example row = spec.nextExample();
        Action action = Action.parse(actionCells(row));
        try {
            Call call = action.checkAgainst(fixture);
            call.will(Do.both(Annotate.right(keywordCells(row))).and(countRowOf(spec).right())).when(ResultIs.equalTo(true));
            call.will(Do.both(Annotate.wrong(keywordCells(row))).and(countRowOf(spec).wrong())).when(ResultIs.equalTo(
                false));
            call.will(Do.both(Annotate.exception(keywordCells(row))).and(countRowOf(spec).exception())).when(ResultIs
                .exception());
            call.execute();
        } catch (Exception e) {
            LOG.error(LOG_ERROR, e);
            keywordCells(row).annotate(Annotations.exception(e));
            reportException(spec);
        }
    }

    private Example keywordCells(Example row) {
        return row.firstChild();
    }
}
