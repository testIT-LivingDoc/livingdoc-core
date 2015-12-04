package info.novatec.testit.livingdoc.fixture.interpreter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import info.novatec.testit.livingdoc.fixture.util.AnnotationTable;
import info.novatec.testit.livingdoc.interpreter.SetOfInterpreter;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;
import info.novatec.testit.livingdoc.util.Rows;
import info.novatec.testit.livingdoc.util.Tables;


@FixtureClass
public class SetOfEmployeeFixture {
    Tables tables;
    List<Employee> employees;

    public void specValues(Tables paramTables) {
        this.tables = paramTables;
    }

    public void sudValues(String values) {
        this.employees = toEmployees(values);
    }

    private List<Employee> toEmployees(String values) {
        ArrayList<Employee> list = new ArrayList<Employee>();
        String[] rows = values.split("\n");

        for (String row : rows) {
            String[] cells = row.split(",");
            list.add(new Employee(cells[0].trim(), cells[1].trim()));
        }
        return list;
    }

    public class Employee {
        public String first;
        public String last;

        public Employee(String first, String last) {
            this.first = first;
            this.last = last;
        }
    }

    public Collection<Employee> query() {
        return employees;
    }

    public AnnotationTable annotations() {
        SetOfInterpreter setOf = new SetOfInterpreter(new PlainOldFixture(this));

        Rows rows = new Rows("first", "last");
        rows.addSibling(tables.firstChild());

        setOf.execute(rows);

        return new AnnotationTable(tables);
    }

}
