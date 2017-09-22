package info.novatec.testit.livingdoc.server.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment;


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
    public void testTheCorrectSyntaxOfTheFixtureFactoryDefinition() {
        SystemUnderTest sut = SystemUnderTest.newInstance("SUT");

        sut.setFixtureFactory("CLASS");
        assertEquals("CLASS", sut.fixtureFactoryCmdLineOption());

        sut.setFixtureFactoryArgs("ARGS");
        assertEquals("CLASS;ARGS", sut.fixtureFactoryCmdLineOption());
    }

}
