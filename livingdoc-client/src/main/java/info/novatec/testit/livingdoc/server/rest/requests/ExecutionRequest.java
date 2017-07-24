package info.novatec.testit.livingdoc.server.rest.requests;

import info.novatec.testit.livingdoc.server.domain.*;

import java.io.*;

public class ExecutionRequest implements Serializable {

    public Runner runner;

    public Specification specification;

    public SystemUnderTest sut;

    public boolean implemented;

    public String section;

    public String locale;


    public ExecutionRequest() {
    }

    public ExecutionRequest(Runner runner, Specification specification, SystemUnderTest sut, boolean implemented, String section, String locale) {
        this.runner = runner;
        this.specification = specification;
        this.sut = sut;
        this.implemented = implemented;
        this.section = section;
        this.locale = locale;
    }
}
