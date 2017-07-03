package info.novatec.testit.livingdoc.server.rest.requests;

import java.util.List;

public class ListDocumentsInHierarchyRequest {

    public List<?> arguments;

    public ListDocumentsInHierarchyRequest() {
    }

    public ListDocumentsInHierarchyRequest(List<?> args) {
        this.arguments = args;
    }
}
