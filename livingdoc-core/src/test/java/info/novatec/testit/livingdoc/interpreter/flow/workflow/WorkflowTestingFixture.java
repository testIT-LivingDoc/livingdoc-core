package info.novatec.testit.livingdoc.interpreter.flow.workflow;

import info.novatec.testit.livingdoc.interpreter.decisiontable.DecisionTableTestingFixture;
import info.novatec.testit.livingdoc.reflect.annotation.Alias;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;

@FixtureClass("Testing the Workflow interpreter")
public class WorkflowTestingFixture {

    
    @Alias("a method returning true")
    public boolean methodReturningTrue(){
        return true;
    }
    
    @Alias("a method returning true with one parameter")
    public boolean methodReturningTrue(String param){
        return true;
    }
    
    @Alias("the method returns a value of")
    public int methodReturningValue(){
        return 12345;
    }
    
    @Alias("a method returning false")
    public boolean methodReturningFalse(){
        return false;
    }
    
    @Alias("an embedded decision table")
    public DecisionTableTestingFixture getEmbeddedDecisionTable(){
        return new DecisionTableTestingFixture();
    }
}
