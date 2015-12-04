package info.novatec.testit.livingdoc.fixture.seeds.comparison;

import info.novatec.testit.livingdoc.TypeConversion;
import info.novatec.testit.livingdoc.expectation.ShouldBe;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


@FixtureClass
public class NumberComparisonFixture {
    public String expectedValue;

    public String systemValue;

    public boolean equal() {
        return ShouldBe.literal(expectedValue).meets(TypeConversion.parse(systemValue, float.class));
    }
}
