package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Requirement;


public class GetSummaryRequest {

    public Requirement requirement;

    public GetSummaryRequest() {
    }

    public GetSummaryRequest(Requirement requirement) {
        this.requirement = requirement;
    }

}
