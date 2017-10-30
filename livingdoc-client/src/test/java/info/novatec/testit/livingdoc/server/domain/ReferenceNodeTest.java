package info.novatec.testit.livingdoc.server.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ReferenceNodeTest {
    ReferenceNode node = new ReferenceNode("TITLE", "REPO-UID", "SUT-NAME", "SECTIONS");

    @Test
    public void testReferenceNodeIsProperlyMarshalled() {
        List<Object> expectedVector = referenceVector();
        node.setIsExecutable(true);
        assertEquals(expectedVector, node.marshallize());
    }


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

    @Test
    public void testReferenceNodeIsProperlyUnmarshallized() {
        DocumentNode hierarchy = XmlRpcDataMarshaller.toDocumentNode(createExpectedVector());
        ReferenceNode childrenNode = ( ReferenceNode ) hierarchy.getChildren().iterator().next();

        assertEquals(childrenNode.getTitle(), "TITLE");
        assertEquals(childrenNode.isExecutable(), true);
        assertEquals(childrenNode.isCanBeImplemented(), false);
        assertEquals(childrenNode.getChildren().size(), 0);
        assertEquals(childrenNode.getRepositoryUID(), "REPO-UID");
        assertEquals(childrenNode.getSutName(), "SUT-NAME");
        assertEquals(childrenNode.getSection(), "SECTIONS");
        }

    private List<Object> referenceVector() {
        List<Object> referenceVector = new ArrayList<Object>();
        referenceVector.add("TITLE");
        referenceVector.add(true); // executable
        referenceVector.add(false); // can be implemented
        referenceVector.add(new Hashtable<String, Object>()); // children
        referenceVector.add("REPO-UID");
        referenceVector.add("SUT-NAME");
        referenceVector.add("SECTIONS");
        return referenceVector;
    }

    private List<Object> createExpectedVector() {
        List<Object> expectedVector = new ArrayList<Object>();
        expectedVector.add("MAIN NODE");
        expectedVector.add(true);
        expectedVector.add(false);
        Hashtable<String, List< ? >> children = new Hashtable<String, List< ? >>();
        children.put("TITLE", referenceVector());
        expectedVector.add(children);
        return expectedVector;
    }
}
