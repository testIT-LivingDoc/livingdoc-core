package info.novatec.testit.livingdoc.server.rest.responses;

import info.novatec.testit.livingdoc.server.domain.Specification;


public class GetSpecificationResponse {

    public Specification specification;

    public GetSpecificationResponse() {
    }

    public GetSpecificationResponse(Specification specification) {
        this.specification = specification;
    }

}
