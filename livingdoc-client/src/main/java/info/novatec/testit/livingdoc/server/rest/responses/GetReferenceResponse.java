package info.novatec.testit.livingdoc.server.rest.responses;

import info.novatec.testit.livingdoc.server.domain.Reference;


public class GetReferenceResponse {

    public Reference reference;

    public GetReferenceResponse() {
    }

    public GetReferenceResponse(Reference reference) {
        this.reference = reference;
    }

}
