package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Repository;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;


public class UpdateSystemUnderTestRequest {

    public String oldSystemUnderTestName;
    public SystemUnderTest newSystemUnderTest;
    public Repository repository;

    public UpdateSystemUnderTestRequest() {
    }

    public UpdateSystemUnderTestRequest(String oldSystemUnderTestName, SystemUnderTest newSystemUnderTest,
                                        Repository repository) {
        this.oldSystemUnderTestName = oldSystemUnderTestName;
        this.newSystemUnderTest = newSystemUnderTest;
        this.repository = repository;
    }

}
