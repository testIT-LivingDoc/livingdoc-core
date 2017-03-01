package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Reference;


public class RunReferenceRequest {

    public Reference reference;
    public String locale;

    public RunReferenceRequest() {
    }

    public RunReferenceRequest(Reference reference, String locale) {
        this.reference = reference;
        this.locale = locale;
    }

}
