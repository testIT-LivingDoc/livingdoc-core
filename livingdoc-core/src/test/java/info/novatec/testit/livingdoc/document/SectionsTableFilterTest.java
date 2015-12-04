package info.novatec.testit.livingdoc.document;

import static info.novatec.testit.livingdoc.util.ExampleUtil.contentOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.util.Tables;


public class SectionsTableFilterTest {
    private SectionsTableFilter filter;

    @Before
    public void setUp() throws Exception {
        filter = new SectionsTableFilter();
    }

    @Test
    public void testShouldNotFilterSpecificationByDefault() throws Exception {
        Example example = Tables.parse("[table]\n" + "[with rows]\n");
        Example next = filter.filter(example);
        assertNotNull(next);
        assertEquals("table", contentOf(next.at(0, 0, 0)));
    }

    @Test
    public void testShouldNotFilterIfNoSectionsAreDefined() throws Exception {
        Example example = Tables.parse("[section]\n" + "[unix]\n" + "****\n" + "[table]\n" + "[with rows]\n" + "****\n");
        Example next = filter.filter(example);
        assertNotNull(next);
        assertEquals("table", contentOf(next.at(0, 0, 0)));
    }

    @Test
    public void testShouldNotFilterTableIfSectionIsIncluded() throws Exception {
        filter.includeSections("unix");
        Example example = Tables.parse("[section]\n" + "[unix]\n" + "****\n" + "[table]\n" + "[with rows]\n" + "****\n");
        Example next = filter.filter(example);
        assertNotNull(next);
        assertEquals("table", contentOf(next.at(0, 0, 0)));
    }

    @Test
    public void testShouldNotFilterTableIfInAGeneralSection() throws Exception {
        filter.includeSections("unix");
        Example example = Tables.parse("[section]\n" + "****\n" + "[table]\n" + "[with rows]\n" + "****\n");
        Example next = filter.filter(example);
        assertNotNull(next);
        assertEquals("table", contentOf(next.at(0, 0, 0)));
    }

    @Test
    public void testShouldSkipTablesUpToNextSectionIfSectionIsNotIncluded() throws Exception {
        filter.includeSections("mac");
        Example example = Tables.parse("[section]\n" + "[unix]\n" + "****\n" + "[unix only]\n" + "****\n" + "[unix only]\n"
            + "****\n" + "[section]");
        Example next = filter.filter(example);
        assertNotNull(next);
        assertEquals("section", contentOf(next.at(0, 0, 0)));
    }
}
