package info.novatec.testit.livingdoc.server.domain;

import static org.junit.Assert.assertEquals;

import java.util.Hashtable;
import java.util.Vector;

import org.junit.Test;

import info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller;


public class DocumentNodeTest {
    @Test
    public void testThatHierarchyIsProperlyMarshallized() {
        assertEquals(vectorHierarchy(), nodeHierarchy().marshallize());
    }

    @Test
    public void testThatANodeCanBeCreateFromAHierarchyInAVector() {
        assertEquals(nodeHierarchy(), XmlRpcDataMarshaller.toDocumentNode(vectorHierarchy()));
    }

    private DocumentNode nodeHierarchy() {
        DocumentNode home = new DocumentNode("HOME");
        home.setIsExecutable(true);
        home.setCanBeImplemented(true);

        DocumentNode node1 = new DocumentNode("Title 1");
        node1.setIsExecutable(true);
        node1.setCanBeImplemented(true);
        DocumentNode node11 = new DocumentNode("Title 11");
        node11.setIsExecutable(true);
        node11.setCanBeImplemented(true);
        node1.addChildren(node11);

        DocumentNode node2 = new DocumentNode("Title 2");
        node2.setIsExecutable(true);
        node2.setCanBeImplemented(true);
        DocumentNode node21 = new DocumentNode("Title 21");
        node21.setIsExecutable(true);
        node21.setCanBeImplemented(true);
        node2.addChildren(node21);
        DocumentNode node22 = new DocumentNode("Title 22");
        node22.setIsExecutable(true);
        node22.setCanBeImplemented(true);
        node2.addChildren(node22);

        home.addChildren(node1);
        home.addChildren(node2);
        return home;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Vector vectorHierarchy() {
        Vector home = new Vector();
        home.add("HOME");
        home.add(true);
        home.add(true);

        Vector node1 = new Vector();
        node1.add("Title 1");
        node1.add(true);
        node1.add(true);
        Vector node11 = new Vector();
        node11.add("Title 11");
        node11.add(true);
        node11.add(true);
        Hashtable child11 = new Hashtable();
        node11.add(child11);
        Hashtable child1 = new Hashtable();
        child1.put("Title 11", node11);
        node1.add(child1);

        Vector node2 = new Vector();
        node2.add("Title 2");
        node2.add(true);
        node2.add(true);
        Vector node21 = new Vector();
        node21.add("Title 21");
        node21.add(true);
        node21.add(true);
        Hashtable child21 = new Hashtable();
        node21.add(child21);
        Vector node22 = new Vector();
        node22.add("Title 22");
        node22.add(true);
        node22.add(true);
        Hashtable child22 = new Hashtable();
        node22.add(child22);
        Hashtable child2 = new Hashtable();
        child2.put("Title 21", node21);
        child2.put("Title 22", node22);
        node2.add(child2);

        Hashtable child = new Hashtable();
        child.put("Title 1", node1);
        child.put("Title 2", node2);
        home.add(child);
        return home;
    }
}
