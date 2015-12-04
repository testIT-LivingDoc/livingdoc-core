package info.novatec.testit.livingdoc.fixture.seeds.comparison;

import info.novatec.testit.livingdoc.expectation.ShouldBe;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


@FixtureClass
public class NullComparisonFixture {
    public String expectedValue;

    public String actualValue;

    public boolean equal() {
        return ShouldBe.literal(expectedValue).meets(actualValue);
    }

    public void setSystemValue(String value) {
        actualValue = value.equals("null") ? null : value;
    }
}
