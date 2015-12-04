package info.novatec.testit.livingdoc.call;

import info.novatec.testit.livingdoc.Annotatable;
import info.novatec.testit.livingdoc.annotation.RightAnnotation;


public class AnnotateRight implements Stub {
    private Annotatable annotatable;

    public AnnotateRight(Annotatable annotatable) {
        this.annotatable = annotatable;
    }

    @Override
    public void call(Result result) {
        annotatable.annotate(new RightAnnotation());
    }
}
