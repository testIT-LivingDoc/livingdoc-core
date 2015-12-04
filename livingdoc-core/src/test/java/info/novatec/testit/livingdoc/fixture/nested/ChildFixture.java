package info.novatec.testit.livingdoc.fixture.nested;

import info.novatec.testit.livingdoc.reflect.annotation.Alias;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


/**
 * The nested fixture.
 * 
 * @author Johannes Schlaudraff
 *
 */
@FixtureClass("Child")
public class ChildFixture {

    @Alias("a")
    public int valueA;
    private int childParameter;

    public ChildFixture(int childParameter) {
        this.childParameter = childParameter;

    }

    @Alias("Parameter from parent")
    public int getValueB() {
        return childParameter;
    }
}
