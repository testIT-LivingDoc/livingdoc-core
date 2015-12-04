package info.novatec.testit.livingdoc.agent.server;

import java.util.Vector;


public interface Service {
    /**
     * Executes the Specification under the given context: Runner /
     * SystemUnderTest.
     * 
     * @param runnerParams the runner params
     * @param sutParams the sut params
     * @param specificationParams the specification params
     * @param implemented is the spec implemented
     * @param sections the sections
     * @param locale the locale
     * @return the Execution of the specification under the given context
     */
    public Vector<Object> execute(Vector<Object> runnerParams, Vector<Object> sutParams, Vector<Object> specificationParams,
        boolean implemented, String sections, String locale);
}
