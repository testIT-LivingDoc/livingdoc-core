package info.novatec.testit.livingdoc.server.rest.responses;

import info.novatec.testit.livingdoc.server.domain.RequirementSummary;


public class GetSummaryResponse {

    public RequirementSummary requirementSummary;

    public GetSummaryResponse() {
    }

    public GetSummaryResponse(RequirementSummary requirementSummary) {
        this.requirementSummary = requirementSummary;
    }

}
