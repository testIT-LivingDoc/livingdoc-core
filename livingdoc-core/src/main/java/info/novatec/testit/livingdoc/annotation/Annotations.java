package info.novatec.testit.livingdoc.annotation;

import java.lang.reflect.InvocationTargetException;

import info.novatec.testit.livingdoc.expectation.Expectation;


public final class Annotations {
    public static ExceptionAnnotation exception(Throwable t) {
        if (t instanceof InvocationTargetException) {
            return new ExceptionAnnotation( ( ( InvocationTargetException ) t ).getTargetException());
        }
        return new ExceptionAnnotation(t);
    }

    public static RightAnnotation right() {
        return new RightAnnotation();
    }

    public static WrongAnnotation wrong() {
        return new WrongAnnotation();
    }

    public static WrongAnnotation wrong(Expectation expected, Object actual) {
        WrongAnnotation annotation = wrong();
        annotation.giveDetails(expected, actual);
        return annotation;
    }

    public static IgnoredAnnotation ignored(Object actual) {
        return new IgnoredAnnotation(actual);
    }

    public static EnteredAnnotation entered() {
        return new EnteredAnnotation();
    }

    public static NotEnteredAnnotation notEntered() {
        return new NotEnteredAnnotation();
    }

    public static Annotation missing() {
        return new MissingAnnotation();
    }

    public static SurplusAnnotation surplus() {
        return new SurplusAnnotation();
    }

    public static SkippedAnnotation skipped() {
        return new SkippedAnnotation();
    }

    public static StoppedAnnotation stopped() {
        return new StoppedAnnotation();
    }

    private Annotations() {
    }
}
