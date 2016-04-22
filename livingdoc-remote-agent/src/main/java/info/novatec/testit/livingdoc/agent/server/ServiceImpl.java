package info.novatec.testit.livingdoc.agent.server;

import info.novatec.testit.livingdoc.server.domain.Execution;
import info.novatec.testit.livingdoc.server.domain.Runner;
import info.novatec.testit.livingdoc.server.domain.Specification;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;
import info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller;

import java.util.List;
import java.util.Vector;


public class ServiceImpl implements Service {

    @Override
    public List<Object> execute(Vector<Object> runnerParams, Vector<Object> sutParams, Vector<Object> specificationParams,
                                boolean implemented, String sections, String locale) {
        Runner runner = XmlRpcDataMarshaller.toRunner(runnerParams);

        // To prevent call forwarding
        runner.setServerName(null);
        runner.setServerPort(null);

        SystemUnderTest systemUnderTest = XmlRpcDataMarshaller.toSystemUnderTest(sutParams);
        Specification specification = XmlRpcDataMarshaller.toSpecification(specificationParams);

        Execution exe = runner.execute(specification, systemUnderTest, implemented, sections, locale);
        return exe.marshallize();
    }
}
