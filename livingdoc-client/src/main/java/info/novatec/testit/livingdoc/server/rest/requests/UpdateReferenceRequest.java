package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Reference;


public class UpdateReferenceRequest {

    public Reference oldReference;
    public Reference newReference;

    public UpdateReferenceRequest() {
    }

    public UpdateReferenceRequest(Reference oldReference, Reference newReference) {
        this.oldReference = oldReference;
        this.newReference = newReference;
    }

}
