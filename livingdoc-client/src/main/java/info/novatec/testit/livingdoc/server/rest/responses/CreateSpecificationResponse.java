package info.novatec.testit.livingdoc.server.rest.responses;

import info.novatec.testit.livingdoc.server.domain.Specification;


public class CreateSpecificationResponse {

    public Specification specification;

    public CreateSpecificationResponse() {
    }

    public CreateSpecificationResponse(Specification specification) {
        this.specification = specification;
    }

}
