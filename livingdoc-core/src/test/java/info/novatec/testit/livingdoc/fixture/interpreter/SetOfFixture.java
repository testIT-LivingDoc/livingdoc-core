package info.novatec.testit.livingdoc.fixture.interpreter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import info.novatec.testit.livingdoc.fixture.util.AnnotationTable;
import info.novatec.testit.livingdoc.interpreter.SetOfInterpreter;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;
import info.novatec.testit.livingdoc.util.Rows;
import info.novatec.testit.livingdoc.util.Tables;


@FixtureClass
public class SetOfFixture {
    Tables tables;
    List<String> values;

    public void specValues(Tables paramTables) {
        this.tables = paramTables;
    }

    public void sudValues(String[] paramValues) {
        this.values = Arrays.asList(paramValues);
    }

    public Collection<String> query() {
        return values;
    }

    public AnnotationTable annotations() {
        SetOfInterpreter setOf = new SetOfInterpreter(new PlainOldFixture(this));

        Rows rows = new Rows("to string");
        rows.addSibling(tables.firstChild());

        setOf.execute(rows);

        return new AnnotationTable(tables);
    }

}
