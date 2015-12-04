package info.novatec.testit.livingdoc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.novatec.testit.livingdoc.Example;


public class Cells extends FakeExample {
    public static Cells parse(String markup) {
        Rows row = new Rows();

        // The following regex uses a group representing the string between [ ]
        // containing any characters except ]
        Pattern pattern = Pattern.compile("\\[([^\\]]*)\\]");
        Matcher matcher = pattern.matcher(markup);

        while (matcher.find()) {
            row.addChild(new Cells(matcher.group(1).trim()));
        }

        return ( Cells ) row.firstChild();
    }

    public static String toMarkup(Example example, boolean prettyPrint) {
        return toMarkup(example, prettyPrint, new MarkupPrinter.Default());
    }

    public static String toMarkup(Example example, boolean prettyPrint, MarkupPrinter printer) {
        StringBuilder sb = new StringBuilder();
        for (Example cell : example) {
            sb.append("[");
            if (prettyPrint) {
                sb.append(" ");
            }
            sb.append(printer.print(cell));
            if (prettyPrint) {
                sb.append(" ");
            }
            sb.append("]");
        }
        return sb.toString();
    }

    public Cells(Object content) {
        super(content);
    }

    @Override
    protected FakeExample newSibling() {
        return new Cells("");
    }

    @Override
    protected FakeExample newChild() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toMarkup(boolean prettyPrint) {
        return toMarkup(this, prettyPrint);
    }

    @Override
    public String toString() {
        return getContent();
    }
}
