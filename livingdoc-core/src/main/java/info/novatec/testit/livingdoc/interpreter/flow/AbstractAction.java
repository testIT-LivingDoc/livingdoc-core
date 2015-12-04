package info.novatec.testit.livingdoc.interpreter.flow;

import static info.novatec.testit.livingdoc.util.CollectionUtil.filter;
import static info.novatec.testit.livingdoc.util.CollectionUtil.joinAsString;
import static info.novatec.testit.livingdoc.util.CollectionUtil.toArray;

import java.util.List;

import info.novatec.testit.livingdoc.Call;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.Message;
import info.novatec.testit.livingdoc.reflect.NoSuchMessageException;
import info.novatec.testit.livingdoc.util.NotBlank;


public abstract class AbstractAction {
    private final Iterable<String> cells;

    public AbstractAction(Iterable<String> cells) {
        this.cells = cells;
    }

    protected Iterable<String> getCells() {
        return this.cells;
    }

    public Call checkAgainst(Fixture fixture) throws NoSuchMessageException {
        return call(fixture.check(name()));
    }

    protected Call call(Message message) throws NoSuchMessageException {
        Call call = new Call(message);
        call.addInput(arguments());
        return call;
    }

    protected abstract List<String> keywords();

    protected abstract List<String> parameters();

    public String[] arguments() {
        return toArray(parameters());
    }

    public String name() {
        return joinAsString(toArray(filter(keywords(), new NotBlank())), " ");
    }

}
