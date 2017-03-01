package info.novatec.testit.livingdoc.server.rest.responses;

import info.novatec.testit.livingdoc.server.domain.Reference;


public class UpdateReferenceResponse {

    public Reference reference;

    public UpdateReferenceResponse() {
    }

    public UpdateReferenceResponse(Reference reference) {
        this.reference = reference;
    }

}
