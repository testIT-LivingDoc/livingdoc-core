package info.novatec.testit.livingdoc.server.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ReferenceNodeTest {
    ReferenceNode node = new ReferenceNode("TITLE", "REPO-UID", "SUT-NAME", "SECTIONS");

    @Test(expected = RuntimeException.class)
    public void testThatReferenceNodeCannotHaveChildren() {
        node.addChildren(new DocumentNode("TITLE-CHILDREN"));
    }

    @Test
    public void testTheEqualBehaviour() {
        assertFalse(node.equals(null));
        assertFalse(node.equals(new DocumentNode("TITLE")));
        assertFalse(node.equals(new ReferenceNode("TITLE-DIFFERENT", "REPO-UID", "SUT-NAME", "SECTIONS")));
        assertFalse(node.equals(new ReferenceNode("TITLE", "REPO-UID-DIFFERENT", "SUT-NAME", "SECTIONS")));
        assertFalse(node.equals(new ReferenceNode("TITLE", "REPO-UID", "SUT-NAME-DIFFERENTE", "SECTIONS")));
        assertFalse(node.equals(new ReferenceNode("TITLE", "REPO-UID", "SUT-NAME-DIFFERENTE", "SECTIONS-DIFFERENTE")));

        assertEquals(node, new ReferenceNode("TITLE", "REPO-UID", "SUT-NAME", "SECTIONS"));
    }
}
