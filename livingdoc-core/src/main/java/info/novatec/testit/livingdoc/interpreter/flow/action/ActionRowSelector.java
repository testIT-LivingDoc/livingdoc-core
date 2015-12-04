package info.novatec.testit.livingdoc.interpreter.flow.action;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.interpreter.flow.Row;
import info.novatec.testit.livingdoc.interpreter.flow.RowSelector;
import info.novatec.testit.livingdoc.reflect.Fixture;


public class ActionRowSelector implements RowSelector {
    private Fixture fixture;

    public ActionRowSelector(Fixture fixture) {
        super();
        this.fixture = fixture;
    }

    @Override
    public Row select(Example example) {
        return new ActionRow(fixture);
    }
}
