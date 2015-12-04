package info.novatec.testit.livingdoc.document;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.util.ExampleUtil;
import info.novatec.testit.livingdoc.util.Tables;


public class EagerFilterTest {

    @Test
    public void testShouldContinueUntilNothingIsLeftToFilter() {
        Tables tables = Tables.parse("****\n" + "[Begin Info]\n" + "****\n" + "[should not be interpreted]" + "****\n"
            + "[End Info]\n" + "****\n" + "[ a table ]\n" + "****\n" + "[Begin Info]\n" + "****\n"
            + "[should not be interpreted]" + "****\n" + "[End Info]\n" + "****\n" + "[Begin Info]\n" + "****\n"
            + "[should not be interpreted]" + "****\n" + "[End Info]\n" + "****\n" + "[ the last table ]\n");

        ExampleFilter filter = new EagerFilter(new CommentTableFilter());
        Example next = filter.filter(tables);
        assertEquals("a table", ExampleUtil.contentOf(next.at(0, 0, 0)));
        next = filter.filter(next.nextSibling());
        assertEquals("the last table", ExampleUtil.contentOf(next.at(0, 0, 0)));
    }
}
