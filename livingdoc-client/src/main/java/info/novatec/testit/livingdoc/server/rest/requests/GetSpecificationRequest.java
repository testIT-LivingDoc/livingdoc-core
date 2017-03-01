package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Specification;


public class GetSpecificationRequest {

    public Specification specification;

    public GetSpecificationRequest() {
    }

    public GetSpecificationRequest(Specification specification) {
        this.specification = specification;
    }

}
