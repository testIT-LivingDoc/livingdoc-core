package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Specification;


public class UpdateSpecificationRequest {

    public Specification oldSpecification;
    public Specification newSpecification;

    public UpdateSpecificationRequest() {
    }

    public UpdateSpecificationRequest(Specification oldSpecification, Specification newSpecification) {
        this.oldSpecification = oldSpecification;
        this.newSpecification = newSpecification;
    }

}
