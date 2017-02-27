package info.novatec.testit.livingdoc.server.rest.responses;

import java.util.List;

public class ListDocumentsInHierarchyResponse {

    private List<?> specifications;

    public ListDocumentsInHierarchyResponse() {}

    public ListDocumentsInHierarchyResponse(List<?> specifications) {
        this.specifications = specifications;
    }

    public List<?> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<?> specifications) {
        this.specifications = specifications;
    }
}
