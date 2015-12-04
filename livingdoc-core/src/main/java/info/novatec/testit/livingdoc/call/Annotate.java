package info.novatec.testit.livingdoc.call;

import info.novatec.testit.livingdoc.Annotatable;
import info.novatec.testit.livingdoc.Example;


public final class Annotate {
    public static Stub right(final Annotatable annotatable) {
        return new AnnotateRight(annotatable);
    }

    public static Stub wrong(Annotatable annotatable) {
        return new AnnotateWrong(annotatable, false);
    }

    public static Stub wrongWithDetails(Annotatable annotatable) {
        return new AnnotateWrong(annotatable, true);
    }

    public static Stub exception(Annotatable annotatable) {
        return new AnnotateException(annotatable);
    }

    private Annotate() {
    }

    public static Stub withDetails(Annotatable annotatable) {
        return new AnnotateExample(annotatable, true);
    }

    public static Stub withoutDetail(Annotatable annotatable) {
        return new AnnotateExample(annotatable, false);
    }

    public static Stub entered(Example annotatable) {
        return new AnnotateEntered(annotatable);
    }

    public static Stub notEntered(Example annotatable) {
        return new AnnotateNotEntered(annotatable);
    }

    public static Stub ignored(Annotatable annotatable) {
        return new AnnotateIgnored(annotatable);
    }

    public static Stub setup(Example annotatable) {
        return new AnnotateSetup(annotatable);
    }
}
