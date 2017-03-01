package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Specification;


public class CreateSpecificationRequest {

    public Specification specification;

    public CreateSpecificationRequest() {
    }

    public CreateSpecificationRequest(Specification specification) {
        this.specification = specification;
    }

}
