package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Reference;


public class CreateReferenceRequest {

    public Reference reference;

    public CreateReferenceRequest() {
    }

    public CreateReferenceRequest(Reference reference) {
        this.reference = reference;
    }

}
