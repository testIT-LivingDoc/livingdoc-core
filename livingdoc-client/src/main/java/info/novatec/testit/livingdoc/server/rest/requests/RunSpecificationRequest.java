package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Specification;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;


public class RunSpecificationRequest {

    public SystemUnderTest systemUnderTest;
    public Specification specification;
    public boolean implementedVersion;
    public String locale;

    public RunSpecificationRequest() {
    }

    public RunSpecificationRequest(SystemUnderTest systemUnderTest, Specification specification, boolean implementedVersion,
        String locale) {
        this.systemUnderTest = systemUnderTest;
        this.specification = specification;
        this.implementedVersion = implementedVersion;
        this.locale = locale;
    }

}
