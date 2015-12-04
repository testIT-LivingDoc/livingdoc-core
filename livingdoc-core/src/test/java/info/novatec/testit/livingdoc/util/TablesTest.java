package info.novatec.testit.livingdoc.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class TablesTest {

    @Test
    public void testBlankLinesInMarkupTable_ShouldBeIgnoredDuringParsing() {
        Tables blankLinesInMarkupTable = new Tables();
        blankLinesInMarkupTable.row(1, 2, 3).row(4, 5, 6).row(7, 8, 9);
        blankLinesInMarkupTable.table().row(1, 2, 3).row(4, 5, 6).row(7, 8, 9);
        String markup =
            "\n\n[ 1 ][ 2 ][ 3 ]\n\n[ 4 ][ 5 ][ 6 ]\n[ 7 ][ 8 ][ 9 ]\n\n****\n\n[ 1 ][ 2 ][ 3 ]\n[ 4 ][ 5 ][ 6 ]\n[ 7 ][ 8 ][ 9 ]\n\n";

        assertEquals(blankLinesInMarkupTable, Tables.parse(markup));
    }

    @Test
    public void testEmptyTable_ShouldBeVisibleAsEmpty() {
        Tables emptyTable = new Tables();
        String markup = "(empty table)";
        assertEquals(markup, emptyTable.toMarkup());
    }

    @Test
    public void testEmptyTable_CanBeIndicatedInMarkup() {
        Tables emptyTable = new Tables();
        String markup = "(empty table)";
        assertEquals(emptyTable, Tables.parse(markup));
    }

    @Test
    public void testEmptyRowInATable_ShouldBeVisibleAsEmpty() {
        Tables emptyRowInATable = new Tables();
        emptyRowInATable.row(1, 2, 3).row().row(7, 8, 9);
        String markup = "[ 1 ][ 2 ][ 3 ]\n(empty row)\n[ 7 ][ 8 ][ 9 ]";
        assertEquals(markup, emptyRowInATable.toMarkup());
    }

    @Test
    public void testEmptyRowInATable_CanBeIndicatedInTheMarkup() {
        Tables emptyRowInATable = new Tables();
        emptyRowInATable.row(1, 2, 3).row().row(7, 8, 9);
        String markup = "[ 1 ][ 2 ][ 3 ]\n(empty row)\n[ 7 ][ 8 ][ 9 ]";
        assertEquals(emptyRowInATable, Tables.parse(markup));
    }

    @Test
    public void testSeveralTables_ShouldBeSeparatedWithARowOfStarsWhenDisplayed() {
        Tables tables = new Tables();
        tables.row(1, 2).row(3, 4);
        tables.table().row(5, 6).row(7, 8);
        String markup = "[ 1 ][ 2 ]\n[ 3 ][ 4 ]\n****\n[ 5 ][ 6 ]\n[ 7 ][ 8 ]";
        assertEquals(markup, tables.toMarkup());
    }

    @Test
    public void testSeveralTables_ShouldBeParsedFromMarkupCorrectly() {
        Tables tables = new Tables();
        tables.row(1, 2).row(3, 4);
        tables.table().row(5, 6).row(7, 8);
        String markup = "[ 1 ][ 2 ]\n[ 3 ][ 4 ]\n****\n[ 5 ][ 6 ]\n[ 7 ][ 8 ]";
        assertEquals(tables, Tables.parse(markup));
    }
}
