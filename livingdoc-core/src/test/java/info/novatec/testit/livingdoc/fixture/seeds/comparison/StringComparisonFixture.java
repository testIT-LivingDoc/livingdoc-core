package info.novatec.testit.livingdoc.fixture.seeds.comparison;

import info.novatec.testit.livingdoc.expectation.ShouldBe;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


@FixtureClass
public class StringComparisonFixture {
    public String expectedValue;

    public String systemValue;

    public boolean equal() {
        return ShouldBe.literal(expectedValue).meets(systemValue);
    }
}
