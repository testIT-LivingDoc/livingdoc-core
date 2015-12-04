package info.novatec.testit.livingdoc.call;

import info.novatec.testit.livingdoc.Annotatable;
import info.novatec.testit.livingdoc.annotation.Annotations;


public class AnnotateException implements Stub {
    private final Annotatable annotatable;

    public AnnotateException(Annotatable annotatable) {
        this.annotatable = annotatable;
    }

    @Override
    public void call(Result result) {
        annotatable.annotate(Annotations.exception(result.getException()));
    }
}
