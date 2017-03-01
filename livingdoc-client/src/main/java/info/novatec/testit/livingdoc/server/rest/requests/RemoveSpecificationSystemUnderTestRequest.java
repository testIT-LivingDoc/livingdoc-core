package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Specification;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;


public class RemoveSpecificationSystemUnderTestRequest {
    public SystemUnderTest systemUnderTest;
    public Specification specification;

    public RemoveSpecificationSystemUnderTestRequest() {
    }

    public RemoveSpecificationSystemUnderTestRequest(SystemUnderTest systemUnderTest, Specification specification) {
        this.systemUnderTest = systemUnderTest;
        this.specification = specification;
    }

}
