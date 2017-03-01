package info.novatec.testit.livingdoc.server.rest.responses;

import info.novatec.testit.livingdoc.server.domain.DocumentNode;


public class GetSpecificationHierarchyResponse {

    public DocumentNode documentNode;

    public GetSpecificationHierarchyResponse() {
    }

    public GetSpecificationHierarchyResponse(DocumentNode documentNode) {
        this.documentNode = documentNode;
    }

}
