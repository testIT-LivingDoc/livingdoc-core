package info.novatec.testit.livingdoc.call;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.annotation.Annotations;


public class AnnotateEntered implements Stub {
    private final Example example;

    public AnnotateEntered(Example example) {
        this.example = example;
    }

    @Override
    public void call(Result result) {
        example.annotate(Annotations.entered());
    }
}
