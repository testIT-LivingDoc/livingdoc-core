package info.novatec.testit.livingdoc.fixture.seeds.comparison;

import info.novatec.testit.livingdoc.expectation.ShouldBe;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;
import info.novatec.testit.livingdoc.util.ClassUtils;


@FixtureClass
public class ErrorComparisonFixture {
    public String expectedValue;

    public Object systemValue;

    public boolean equal() {
        return ShouldBe.literal(expectedValue).meets(systemValue);
    }

    public void setSystemValue(String value) {
        try {
            systemValue = ClassUtils.loadClass(value).newInstance();
        } catch (Exception e) {
            systemValue = value;
        }
    }
}
