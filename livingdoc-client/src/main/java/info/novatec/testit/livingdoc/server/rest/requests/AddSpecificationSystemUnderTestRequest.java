package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Specification;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;


public class AddSpecificationSystemUnderTestRequest {

    public SystemUnderTest systemUnderTest;
    public Specification specification;

    public AddSpecificationSystemUnderTestRequest() {
    }

    public AddSpecificationSystemUnderTestRequest(SystemUnderTest systemUnderTest, Specification specification) {
        this.systemUnderTest = systemUnderTest;
        this.specification = specification;
    }

}
