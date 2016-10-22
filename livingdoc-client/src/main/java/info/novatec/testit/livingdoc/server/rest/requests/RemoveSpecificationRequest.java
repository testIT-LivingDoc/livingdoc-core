package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Specification;


public class RemoveSpecificationRequest {

    public Specification specification;

    public RemoveSpecificationRequest() {
    }

    public RemoveSpecificationRequest(Specification specification) {
        this.specification = specification;
    }

}
