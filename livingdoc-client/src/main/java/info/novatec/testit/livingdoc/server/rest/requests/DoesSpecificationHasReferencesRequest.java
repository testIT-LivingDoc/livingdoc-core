package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Specification;


public class DoesSpecificationHasReferencesRequest {
    public Specification specification;

    public DoesSpecificationHasReferencesRequest() {
    }

    public DoesSpecificationHasReferencesRequest(Specification specification) {
        this.specification = specification;
    }

}
