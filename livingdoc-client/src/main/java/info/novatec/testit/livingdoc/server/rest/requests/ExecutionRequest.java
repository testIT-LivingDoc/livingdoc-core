package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.Runner;
import info.novatec.testit.livingdoc.server.domain.Specification;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutionRequest {

    private Runner runner;

    private Specification specification;

    private SystemUnderTest systemUnderTest;

    private boolean implemented;

    private String section;

    private String locale;

    public ExecutionRequest(Runner runner, Specification specification,
                            SystemUnderTest systemUnderTest, boolean implemented, String section, String locale) {
        this.runner = runner;
        this.specification = specification;
        this.systemUnderTest = systemUnderTest;
        this.implemented = implemented;
        this.section = section;
        this.locale = locale;
    }

    public Runner getRunner() {
        return runner;
    }

    public void setRunner(Runner runner) {
        this.runner = runner;
    }

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public SystemUnderTest getSystemUnderTest() {
        return systemUnderTest;
    }

    public void setSystemUnderTest(SystemUnderTest systemUnderTest) {
        this.systemUnderTest = systemUnderTest;
    }

    public boolean isImplemented() {
        return implemented;
    }

    public void setImplemented(boolean implemented) {
        this.implemented = implemented;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
