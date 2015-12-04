package info.novatec.testit.livingdoc.fixture.nested;

import info.novatec.testit.livingdoc.reflect.annotation.Alias;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


/**
 * The parent fixture.
 * 
 * @author Johannes Schlaudraff
 *
 */
@FixtureClass("Parent")
public class ParentRuleForFixture {

    private int childParameter;

    @Alias("myAction")
    public void theAction(int childParameter) {
        this.childParameter = childParameter;
    }

    @Alias("Child")
    public ChildFixture getRuleForChild() {
        return new ChildFixture(this.childParameter);
    }
}
