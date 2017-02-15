package info.novatec.testit.livingdoc.server.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Hashtable;
import java.util.Vector;

import org.junit.Test;

import info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller;


public class ReferenceNodeTest {
    ReferenceNode node = new ReferenceNode("TITLE", "REPO-UID", "SUT-NAME", "SECTIONS");

    @Test
    public void testReferenceNodeIsProperlyMarshalled() {
        Vector<Object> expectedVector = referenceVector();
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

    private Vector<Object> referenceVector() {
        Vector<Object> referenceVector = new Vector<Object>();
        referenceVector.add("TITLE");
        referenceVector.add(true); // executable
        referenceVector.add(false); // can be implemented
        referenceVector.add(new Hashtable<String, Object>()); // children
        referenceVector.add("REPO-UID");
        referenceVector.add("SUT-NAME");
        referenceVector.add("SECTIONS");
        return referenceVector;
    }

    private Vector<Object> createExpectedVector() {
        Vector<Object> expectedVector = new Vector<Object>();
        expectedVector.add("MAIN NODE");
        expectedVector.add(true);
        expectedVector.add(false);
        Hashtable<String, Vector< ? >> children = new Hashtable<String, Vector< ? >>();
        children.put("TITLE", referenceVector());
        expectedVector.add(children);
        return expectedVector;
    }
}
