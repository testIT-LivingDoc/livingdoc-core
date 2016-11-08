package info.novatec.testit.livingdoc.interpreter.flow.action;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.interpreter.flow.Row;
import info.novatec.testit.livingdoc.interpreter.flow.RowSelector;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;
import info.novatec.testit.livingdoc.util.Rows;

@Deprecated
public class ActionRowSelectorTest {

    RowSelector rowSelector;

    @Before
    public void setUp() throws Exception {
        rowSelector = new ActionRowSelector(new PlainOldFixture(new Object()));
    }

    @Test
    public void testThatItAlwaysSelectsAnActionRow() {
        Rows rows = Rows.parse("[ anything ]");

        Row row = rowSelector.select(rows);
        assertThat(row, is(instanceOf(ActionRow.class)));
    }

}
