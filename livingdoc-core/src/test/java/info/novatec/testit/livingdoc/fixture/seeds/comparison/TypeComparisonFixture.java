package info.novatec.testit.livingdoc.fixture.seeds.comparison;

import info.novatec.testit.livingdoc.expectation.ShouldBe;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;
import info.novatec.testit.livingdoc.util.ClassUtils;


@FixtureClass
public class TypeComparisonFixture {
    public String expectedValue;

    public String systemValueType;

    public boolean equal() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return ShouldBe.instanceOf(ClassUtils.loadClass(expectedValue)).meets(ClassUtils.loadClass(systemValueType)
            .newInstance());
    }
}
