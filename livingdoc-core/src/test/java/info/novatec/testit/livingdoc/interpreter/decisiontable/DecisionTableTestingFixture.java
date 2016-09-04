package info.novatec.testit.livingdoc.interpreter.decisiontable;

import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;

@FixtureClass("Testing the decision table interpreter")
public class DecisionTableTestingFixture {
   
    private int input1;
    private int input2;
    
    
    public void setInput1(int input){
        input1 = input;
    }
    
    public void setInput2(int input){
        input2 = input;
    }
    
    public int getExpected1(){
        return input1;
    }
    
    public int getExpected2(){
        return input2;
    }
}
