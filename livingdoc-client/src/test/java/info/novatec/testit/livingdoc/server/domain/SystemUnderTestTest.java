package info.novatec.testit.livingdoc.server.domain;

import static org.junit.Assert.assertEquals;

import info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller;
import org.junit.Test;

import info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment;

import java.util.ArrayList;
import java.util.List;


public class SystemUnderTestTest {
    @Test
    public void testTheSutIsAlphaNaturalComparableWithItsName() {
        assertEquals(0, SystemUnderTest.newInstance("SUT-1").compareTo(SystemUnderTest.newInstance("SUT-1")));
        assertEquals( - 1, SystemUnderTest.newInstance("SUT-1").compareTo(SystemUnderTest.newInstance("SUT-2")));
        assertEquals(1, SystemUnderTest.newInstance("SUT-2").compareTo(SystemUnderTest.newInstance("SUT-1")));
    }

    @Test
    public void testDefaultSutIsAlwaysLowerThenOtherSUT() {
        SystemUnderTest sut1 = SystemUnderTest.newInstance("SUT-1");
        SystemUnderTest sut2 = SystemUnderTest.newInstance("SUT-2");

        assertEquals(1, sut2.compareTo(sut1));
        sut2.setIsDefault(true);
        assertEquals( - 1, sut2.compareTo(sut1));
    }

    @Test
    public void testTheFixtureFactoryDefinitionByDefaultIsDefaultSystemUnderDevString() {
        SystemUnderTest sut = SystemUnderTest.newInstance("SUT");
        assertEquals(DefaultSystemUnderDevelopment.class.getName(), sut.fixtureFactoryCmdLineOption());
    }

    @Test
    public void testSystemUnderTestIsProperlyMarshalled() {
        Runner runner = Runner.newInstance("RUNNER-1");

        SystemUnderTest sut = SystemUnderTest.newInstance("SUT-1");
        sut.setProject(Project.newInstance("PROJECT-1"));
        sut.setSutClasspaths(getSutPaths());
        sut.setFixtureClasspaths(getFixturePaths());
        sut.setFixtureFactory("FIXTUREFACTORY-1");
        sut.setFixtureFactoryArgs("FIXTUREFACTORYARGS-1");
        sut.setIsDefault(true);
        sut.setRunner(runner);
        sut.setProjectDependencyDescriptor("PROJECT-DEPENDENCY-DESCRIPTOR-11");

        List<Object> params = new ArrayList<Object>();
        params.add(XmlRpcDataMarshaller.SUT_NAME_IDX, "SUT-1");
        List<Object> pparams = new ArrayList<Object>();
        pparams.add(XmlRpcDataMarshaller.PROJECT_NAME_IDX, "PROJECT-1");
        params.add(XmlRpcDataMarshaller.SUT_PROJECT_IDX, pparams);
        params.add(XmlRpcDataMarshaller.SUT_CLASSPATH_IDX, new ArrayList<String>(getSutPaths()));
        params.add(XmlRpcDataMarshaller.SUT_FIXTURE_CLASSPATH_IDX, new ArrayList<String>(getFixturePaths()));
        params.add(XmlRpcDataMarshaller.SUT_FIXTURE_FACTORY_IDX, "FIXTUREFACTORY-1");
        params.add(XmlRpcDataMarshaller.SUT_FIXTURE_FACTORY_ARGS_IDX, "FIXTUREFACTORYARGS-1");
        params.add(XmlRpcDataMarshaller.SUT_IS_DEFAULT_IDX, true);
        params.add(XmlRpcDataMarshaller.SUT_RUNNER_IDX, runner.marshallize());
        params.add(XmlRpcDataMarshaller.SUT_PROJECT_DEPENDENCY_DESCRIPTOR_IDX, "PROJECT-DEPENDENCY-DESCRIPTOR-11");

        assertEquals(params, sut.marshallize());
    }

    @Test
    public void testTheCorrectSyntaxOfTheFixtureFactoryDefinition() {
        SystemUnderTest sut = SystemUnderTest.newInstance("SUT");

        sut.setFixtureFactory("CLASS");
        assertEquals("CLASS", sut.fixtureFactoryCmdLineOption());

        sut.setFixtureFactoryArgs("ARGS");
        assertEquals("CLASS;ARGS", sut.fixtureFactoryCmdLineOption());
    }

    private ClasspathSet getSutPaths() {
        ClasspathSet sutPaths = new ClasspathSet();
        sutPaths.add("SUT-PATH-1");
        sutPaths.add("SUT-PATH-2");
        return sutPaths;
    }

    private ClasspathSet getFixturePaths() {
        ClasspathSet fixturePaths = new ClasspathSet();
        fixturePaths.add("FIX-PATH-1");
        fixturePaths.add("FIX-PATH-2");
        return fixturePaths;
    }

}
