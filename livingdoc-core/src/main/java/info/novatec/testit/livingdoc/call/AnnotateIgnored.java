package info.novatec.testit.livingdoc.call;

import info.novatec.testit.livingdoc.Annotatable;
import info.novatec.testit.livingdoc.annotation.Annotations;


public class AnnotateIgnored implements Stub {
    private final Annotatable annotatable;

    public AnnotateIgnored(Annotatable annotatable) {
        this.annotatable = annotatable;
    }

    @Override
    public void call(Result result) {
        annotatable.annotate(Annotations.ignored(result.getActual()));
    }
}
