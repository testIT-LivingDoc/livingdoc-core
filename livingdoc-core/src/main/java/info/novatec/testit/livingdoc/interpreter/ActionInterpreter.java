package info.novatec.testit.livingdoc.interpreter;

import info.novatec.testit.livingdoc.interpreter.flow.AbstractFlowInterpreter;
import info.novatec.testit.livingdoc.interpreter.flow.action.ActionRowSelector;
import info.novatec.testit.livingdoc.reflect.Fixture;


public class ActionInterpreter extends AbstractFlowInterpreter {
    public ActionInterpreter(Fixture fixture) {
        super(fixture);
        setRowSelector(new ActionRowSelector(fixture));
    }
}
