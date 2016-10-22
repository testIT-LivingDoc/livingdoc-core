package info.novatec.testit.livingdoc.server.rest.requests;

public class GetSystemUnderTestsOfProjectRequest {
    public String projectName;

    public GetSystemUnderTestsOfProjectRequest() {
    }

    public GetSystemUnderTestsOfProjectRequest(String projectName) {
        this.projectName = projectName;
    }

}
