/**
 *
 */
package info.novatec.testit.livingdoc.fixture.util;

import org.apache.commons.lang3.StringUtils;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.util.MarkupPrinter;
import info.novatec.testit.livingdoc.util.Tables;


public class AnnotationTable {
    Example tables;

    public AnnotationTable(Example tables) {
        this.tables = tables;
    }

    @Override
    public String toString() {
        return Tables.toMarkup(tables, false, new AnnotationMarkupPrinter());
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if ( ! ( object instanceof AnnotationTable )) {
            return false;
        }

        AnnotationTable other = ( AnnotationTable ) object;

        return this.toString().equals(other.toString());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static AnnotationTable parse(String markup) {
        AnnotationTable table = new AnnotationTable(Tables.parse(markup));
        return table;
    }

    public static class AnnotationMarkupPrinter implements MarkupPrinter {

        @Override
        public String print(Example cell) {
            String content = cell.getContent();
            String spacer = StringUtils.isBlank(content) ? "" : " ";

            return content + ( AnnotationUtil.hasAnnotation(cell) ? spacer + AnnotationUtil.getAnnotationOnCell(cell) : "" );
        }
    }

}
