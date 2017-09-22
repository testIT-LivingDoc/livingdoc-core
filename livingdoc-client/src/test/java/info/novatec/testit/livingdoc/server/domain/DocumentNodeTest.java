package info.novatec.testit.livingdoc.server.domain;

public class DocumentNodeTest {

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

}
