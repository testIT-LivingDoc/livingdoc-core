package info.novatec.testit.livingdoc.document;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Interpreter;


public interface InterpreterSelector {
    Interpreter selectInterpreter(Example table);
}
