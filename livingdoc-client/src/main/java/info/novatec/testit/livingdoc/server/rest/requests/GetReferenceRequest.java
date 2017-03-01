package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Reference;


public class GetReferenceRequest {

    public Reference reference;

    public GetReferenceRequest() {
    }

    public GetReferenceRequest(Reference reference) {
        this.reference = reference;
    }

}
