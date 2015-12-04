package info.novatec.testit.livingdoc.call;

import info.novatec.testit.livingdoc.Annotatable;


public class AnnotateExample implements Stub {
    private final Annotatable annotatable;
    private final boolean detailed;

    public AnnotateExample(Annotatable annotatable, boolean detailed) {
        this.annotatable = annotatable;
        this.detailed = detailed;
    }

    @Override
    public void call(Result result) {
        if (result.isRight()) {
            Annotate.right(annotatable).call(result);
        }
        if (result.isWrong()) {
            new AnnotateWrong(annotatable, detailed).call(result);
        }
        if (result.isException()) {
            Annotate.exception(annotatable).call(result);
        }
        if (result.isIgnored()) {
            Annotate.ignored(annotatable).call(result);
        }
    }
}
