package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Reference;


public class RemoveReferenceRequest {

    public Reference reference;

    public RemoveReferenceRequest() {
    }

    public RemoveReferenceRequest(Reference reference) {
        this.reference = reference;
    }

}
