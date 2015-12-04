/**
 * 
 */
package info.novatec.testit.livingdoc.interpreter.column;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Statistics;


public class NullColumn extends Column {
    @Override
    public Statistics doCell(Example cell) {
        return new Statistics();
    }
}
