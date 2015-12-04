package info.novatec.testit.livingdoc.document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.util.ExampleUtil;
import info.novatec.testit.livingdoc.util.Tables;


public class LivingDocTableFilterTest {

    @Test
    public void testShouldSkipSpecificationInLazyModeIfNoLivingDocTestTags() {
        LivingDocTableFilter filter = new LivingDocTableFilter(true);

        Example example = Tables.parse("[table]\n" + "[with rows]\n");
        assertTrue(filter.canFilter(example));
        assertNull(filter.filter(example));
    }

    @Test
    public void testShouldSkipSpecificationInLazyModeIfNoLivingDocEndTestTag() {
        LivingDocTableFilter filter = new LivingDocTableFilter(true);

        Example example = Tables.parse("[" + LivingDocTableFilter.BEGIN_TEST + "]\n" + "****\n" + "[table]\n"
            + "[with rows]\n");
        assertTrue(filter.canFilter(example));
        assertNull(filter.filter(example));
    }

    @Test
    public void testShouldNotSkipSpecificationInEagerModeIfNoLivingDocTestTags() {
        LivingDocTableFilter filter = new LivingDocTableFilter(false);

        Example example = Tables.parse("[table]\n" + "[with rows]\n");
        assertFalse(filter.canFilter(example));
        assertEquals("table", ExampleUtil.contentOf(filter.filter(example).at(0, 0, 0)));
    }

    @Test
    public void testShouldNotSkipSpecificationInLazyModeIfNoLivingDocEndTestTag() {
        LivingDocTableFilter filter = new LivingDocTableFilter(false);

        Example example = Tables.parse("[" + LivingDocTableFilter.BEGIN_TEST + "]\n" + "****\n" + "[table]\n"
            + "[with rows]\n");
        assertTrue(filter.canFilter(example));
        assertEquals("table", ExampleUtil.contentOf(filter.filter(example).at(0, 0, 0)));
    }

    @Test
    public void testShouldSkipSpecificationInLazyModeToEndLivingDocTag() {
        LivingDocTableFilter filter = new LivingDocTableFilter(true);

        Example example = Tables.parse("[table]\n" + "[with rows]\n" + "****\n" + "[" + LivingDocTableFilter.BEGIN_TEST
            + "]\n" + "****\n" + "[table1]\n" + "****\n" + "[" + LivingDocTableFilter.END_TEST + "]\n");
        assertTrue(filter.canFilter(example));
        Example filtered = filter.filter(example);
        assertNotNull(filtered);
        assertEquals("table1", ExampleUtil.contentOf(filtered.at(0, 0, 0)));
    }

    @Test
    public void testShouldSkipLivingDocTagInNotLazyMode() {
        LivingDocTableFilter filter = new LivingDocTableFilter(false);

        Example example = Tables.parse("[table]\n" + "[with rows]\n" + "****\n" + "[" + LivingDocTableFilter.BEGIN_TEST
            + "]\n" + "****\n" + "[table1]\n");
        assertFalse(filter.canFilter(example));
        Example filtered = example;
        filtered = filter.filter(filtered);
        assertEquals("table", ExampleUtil.contentOf(filtered.at(0, 0, 0)));

        assertTrue(filter.canFilter(filtered.nextSibling()));
        filtered = filter.filter(filtered.nextSibling());
        assertNotNull(filtered);
        assertEquals("table1", ExampleUtil.contentOf(filtered.at(0, 0, 0)));
    }

    @Test
    public void testShouldGetOnlyElementsWithingLivingDocTags() {
        LivingDocTableFilter filter = new LivingDocTableFilter(true);

        Example example = Tables.parse("[table]\n" + "[with rows]\n" + "****\n" + "[" + LivingDocTableFilter.BEGIN_TEST
            + "]\n" + "****\n" + "[table1]\n" + "****\n" + "[" + LivingDocTableFilter.END_TEST + "]\n" + "****\n"
            + "[table2]\n" + "****\n");
        assertTrue(filter.canFilter(example));
        Example filtered = filter.filter(example);

        assertNotNull(filtered);
        assertEquals("table1", ExampleUtil.contentOf(filtered.at(0, 0, 0)));

        assertTrue(filter.canFilter(filtered.nextSibling()));
        filtered = filter.filter(filtered.nextSibling());

        assertNull(filtered);
    }

    @Test
    public void testShouldSkipBeginAndEndLivingDocTags() {
        LivingDocTableFilter filter = new LivingDocTableFilter(false);

        Example example = Tables.parse("[table]\n" + "[with rows]\n" + "****\n" + "[" + LivingDocTableFilter.BEGIN_TEST
            + "]\n" + "****\n" + "[table1]\n" + "****\n" + "[" + LivingDocTableFilter.END_TEST + "]\n" + "****\n"
            + "[table2]\n" + "****\n");
        Example filtered = filter.filter(example);

        assertNotNull(filtered);
        assertEquals("table", ExampleUtil.contentOf(filtered.at(0, 0, 0)));

        filtered = filter.filter(filtered.nextSibling());

        assertNotNull(filtered);
        assertEquals("table1", ExampleUtil.contentOf(filtered.at(0, 0, 0)));

        filtered = filter.filter(filtered.nextSibling());

        assertNotNull(filtered);
        assertEquals("table2", ExampleUtil.contentOf(filtered.at(0, 0, 0)));
    }

    @Test
    public void testShouldGetAllElementsAfterBeginLivingDocTagToTheEndLivingDocTag() {
        LivingDocTableFilter filter = new LivingDocTableFilter(true);

        Example example = Tables.parse("[table]\n" + "[with rows]\n" + "****\n" + "[" + LivingDocTableFilter.BEGIN_TEST
            + "]\n" + "****\n" + "[table1]\n" + "****\n" + "[table2]\n" + "****\n" + "[" + LivingDocTableFilter.END_TEST
            + "]\n");
        assertTrue(filter.canFilter(example));
        Example filtered = filter.filter(example);

        assertNotNull(filtered);
        assertEquals("table1", ExampleUtil.contentOf(filtered.at(0, 0, 0)));

        assertFalse(filter.canFilter(filtered.nextSibling()));
        filtered = filter.filter(filtered.nextSibling());
        assertEquals("table2", ExampleUtil.contentOf(filtered.at(0, 0, 0)));

        assertTrue(filter.canFilter(filtered.nextSibling()));
        filtered = filter.filter(filtered.nextSibling());

        assertNull(filtered);
    }

    @Test
    public void testWeCanCombineLivingDocTags() {
        LivingDocTableFilter filter = new LivingDocTableFilter(true);

        Example example = Tables.parse("[table]\n" + "[with rows]\n" + "****\n" + "[" + LivingDocTableFilter.BEGIN_TEST
            + "]\n" + "****\n" + "[table1]\n" + "****\n" + "[" + LivingDocTableFilter.BEGIN_TEST + "]\n" + "****\n" + "["
            + LivingDocTableFilter.BEGIN_TEST + "]\n" + "****\n" + "[table2]\n" + "****\n" + "["
            + LivingDocTableFilter.BEGIN_TEST + "]\n" + "****\n" + "[table2bis]\n" + "****\n" + "["
            + LivingDocTableFilter.END_TEST + "]\n" + "****\n" + "[" + LivingDocTableFilter.END_TEST + "]\n" + "****\n" + "["
            + LivingDocTableFilter.END_TEST + "]\n" + "****\n" + "[table3]\n" + "****\n" + "["
            + LivingDocTableFilter.END_TEST + "]\n" + "****\n" + "[table4]\n");

        assertTrue(filter.canFilter(example));
        Example filtered = filter.filter(example);

        assertNotNull(filtered);
        assertEquals("table1", ExampleUtil.contentOf(filtered.at(0, 0, 0)));

        assertTrue(filter.canFilter(filtered.nextSibling()));
        filtered = filter.filter(filtered.nextSibling());
        assertEquals("table2", ExampleUtil.contentOf(filtered.at(0, 0, 0)));

        assertTrue(filter.canFilter(filtered.nextSibling()));
        filtered = filter.filter(filtered.nextSibling());
        assertEquals("table2bis", ExampleUtil.contentOf(filtered.at(0, 0, 0)));

        assertTrue(filter.canFilter(filtered.nextSibling()));
        filtered = filter.filter(filtered.nextSibling());

        assertNotNull(filtered);
        assertEquals("table3", ExampleUtil.contentOf(filtered.at(0, 0, 0)));

        assertTrue(filter.canFilter(filtered.nextSibling()));
        filtered = filter.filter(filtered.nextSibling());

        assertNull(filtered);
    }
}
