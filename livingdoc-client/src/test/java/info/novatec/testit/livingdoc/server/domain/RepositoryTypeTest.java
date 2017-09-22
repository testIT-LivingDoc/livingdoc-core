package info.novatec.testit.livingdoc.server.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import org.junit.Test;



public class RepositoryTypeTest {
    @Test
    public void testTheBasicBehaviorOfTheEquals() {
        assertFalse(RepositoryType.newInstance("TYPE-1").equals(null));
        assertFalse(RepositoryType.newInstance("TYPE-1").equals(new Integer(0)));
        assertFalse(RepositoryType.newInstance("TYPE-1").equals(RepositoryType.newInstance("TYPE-2")));
        assertTrue(RepositoryType.newInstance("TYPE-1").equals(RepositoryType.newInstance("TYPE-1")));
    }

    @Test
    public void testThatTheRepositoryTypeIsAlphaComparable() {
        assertEquals(0, RepositoryType.newInstance("TYPE-1").compareTo(RepositoryType.newInstance("TYPE-1")));
        assertEquals( - 1, RepositoryType.newInstance("TYPE-1").compareTo(RepositoryType.newInstance("TYPE-2")));
        assertEquals(1, RepositoryType.newInstance("TYPE-2").compareTo(RepositoryType.newInstance("TYPE-1")));
    }

    @Test
    public void testThatTheResolveNameReturnsTheFormatedString() {
        RepositoryType type = RepositoryType.newInstance("TYPE-1");
        type.setDocumentUrlFormat("%s%s");
        Repository repository = Repository.newInstance("UID-1");
        repository.setBaseRepositoryUrl("URL-1");
        repository.setBaseTestUrl("URI-1");

        assertEquals("URL-1DOCUMENT", type.resolveName(new TestMyDocument(repository, "DOCUMENT")));
    }

    @Test
    public void testThatTheResolveURIReturnsTheFormatedString() {
        RepositoryType type = RepositoryType.newInstance("TYPE-1");
        type.setTestUrlFormat("%s%s");
        Repository repository = Repository.newInstance("UID-1");
        repository.setBaseTestUrl("URI-1");

        assertEquals("URI-1DOCUMENT", type.resolveUri(new TestMyDocument(repository, "DOCUMENT")));
    }

    class TestMyDocument extends Document {
        private static final long serialVersionUID = 1L;

        TestMyDocument(Repository repository, String name) {
            super();
            setRepository(repository);
            setName(name);
        }
    }
}
