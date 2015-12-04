package info.novatec.testit.livingdoc.util;

import org.apache.commons.lang3.StringUtils;

import info.novatec.testit.livingdoc.Example;


public class Tables extends FakeExample {
    private static final String EMPTY = "(empty table)";

    public static Tables parse(String markup) {
        Doc doc = new Doc();
        String[] tables = markup.split("\\*{4}");
        for (String rows : tables) {
            if (StringUtils.isBlank(rows)) {
                continue;
            }
            Tables table = ( Tables ) doc.addChild();
            if ( ! empty(rows)) {
                table.addChild(Rows.parse(rows));
            }
        }
        return ( Tables ) doc.firstChild();
    }

    public static String toMarkup(Example example, boolean prettyPrint) {
        return toMarkup(example, prettyPrint, new MarkupPrinter.Default());
    }

    public static String toMarkup(Example example, boolean prettyPrint, MarkupPrinter printer) {
        StringBuilder sb = new StringBuilder();
        if (example.hasChild()) {
            sb.append(Rows.toMarkup(example.firstChild(), prettyPrint, printer));
        } else {
            sb.append(EMPTY);
        }

        if (example.hasSibling()) {
            sb.append("\n****\n");
            sb.append(toMarkup(example.nextSibling(), prettyPrint, printer));
        }
        return sb.toString();
    }

    private static boolean empty(String rows) {
        return EMPTY.equals(rows);
    }

    public Tables table() {
        return ( Tables ) addSibling();
    }

    public Tables row(Object... cells) {
        addChild(new Rows(cells));
        return this;
    }

    @Override
    public Tables newSibling() {
        return new Tables();
    }

    @Override
    public Rows newChild() {
        return new Rows();
    }

    @Override
    public String toString() {
        return hasChild() ? parts.toMarkup(false) : EMPTY;
    }

    @Override
    public String toMarkup(boolean prettyPrint) {
        return toMarkup(this, prettyPrint);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if ( ! ( object instanceof Example )) {
            return false;
        }

        Example other = ( Example ) object;
        return toMarkup(false).equals(toMarkup(other, false));
    }

    private static class Doc extends FakeExample {
        @Override
        public Doc newSibling() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Tables newChild() {
            return new Tables();
        }

        @Override
        public String toMarkup(boolean prettyPrint) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return hasChild() ? firstChild().toMarkup(false) : "";
        }
    }
}
