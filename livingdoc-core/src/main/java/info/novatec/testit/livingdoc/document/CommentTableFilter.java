package info.novatec.testit.livingdoc.document;

import static info.novatec.testit.livingdoc.util.ExampleUtil.contentOf;

import info.novatec.testit.livingdoc.Example;


public class CommentTableFilter extends AbstractTableFilter {
    private static final String BEGIN_INFO = "begin info";
    private static final String END_INFO = "end info";

    public CommentTableFilter() {
        super(BEGIN_INFO);
    }

    @Override
    protected Example doFilter(Example example) {
        for (Example table = example.nextSibling(); table != null; table = table.nextSibling()) {
            if (END_INFO.equalsIgnoreCase(contentOf(table.at(0, 0, 0)))) {
                return table.nextSibling();
            }
        }
        return null;
    }
}
