package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Specification;


public class GetSpecificationReferencesRequest {
    public Specification specification;

    public GetSpecificationReferencesRequest() {
    }

    public GetSpecificationReferencesRequest(Specification specification) {
        this.specification = specification;
    }

}
