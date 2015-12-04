package info.novatec.testit.livingdoc.util;

import info.novatec.testit.livingdoc.Example;


public interface MarkupPrinter {

    String print(Example cell);

    public static class Default implements MarkupPrinter {
        @Override
        public String print(Example cell) {
            return cell.getContent();
        }
    }
}
