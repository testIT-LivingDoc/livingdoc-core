package info.novatec.testit.livingdoc.fixture.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.annotation.Annotation;
import info.novatec.testit.livingdoc.annotation.Annotations;
import info.novatec.testit.livingdoc.annotation.EnteredAnnotation;
import info.novatec.testit.livingdoc.annotation.ExceptionAnnotation;
import info.novatec.testit.livingdoc.annotation.IgnoredAnnotation;
import info.novatec.testit.livingdoc.annotation.MissingAnnotation;
import info.novatec.testit.livingdoc.annotation.NotEnteredAnnotation;
import info.novatec.testit.livingdoc.annotation.RightAnnotation;
import info.novatec.testit.livingdoc.annotation.SkippedAnnotation;
import info.novatec.testit.livingdoc.annotation.SurplusAnnotation;
import info.novatec.testit.livingdoc.annotation.WrongAnnotation;
import info.novatec.testit.livingdoc.util.BreachSpecificationEncapsulation;
import info.novatec.testit.livingdoc.util.DuckType;


public final class AnnotationUtil {
    private AnnotationUtil() {
    }

    public static Integer[] cellsMarkedRight(Example row) {
        List<Integer> rights = new ArrayList<Integer>();

        int index = 1;

        for (Example cell : row.firstChild()) {
            if (isAnnotatedWith(cell, RightAnnotation.class)) {
                rights.add(index);
            }
            index ++ ;
        }
        return rights.toArray(new Integer[rights.size()]);
    }

    public static Integer[] cellsMarkedWrong(Example row) {
        List<Integer> wrongs = new ArrayList<Integer>();

        int index = 1;

        for (Example cell : row.firstChild()) {
            if (isAnnotatedWith(cell, WrongAnnotation.class)) {
                wrongs.add(index);
            }
            index ++ ;
        }
        return wrongs.toArray(new Integer[wrongs.size()]);
    }

    public static Integer[] cellsMarkedException(Example row) {
        List<Integer> errors = new ArrayList<Integer>();

        int index = 1;

        for (Example cell : row.firstChild()) {
            if (isAnnotatedWith(cell, ExceptionAnnotation.class)) {
                errors.add(index);
            }
            index ++ ;
        }
        return errors.toArray(new Integer[errors.size()]);
    }

    public static Integer[] cellsMarkedIgnored(Example row) {
        List<Integer> errors = new ArrayList<Integer>();

        int index = 1;

        for (Example cell : row.firstChild()) {
            if (isAnnotatedWith(cell, IgnoredAnnotation.class)) {
                errors.add(index);
            }
            index ++ ;
        }
        return errors.toArray(new Integer[errors.size()]);
    }

    public static Integer[] cellsMarked(Example row) {
        List<Integer> marked = new ArrayList<Integer>();

        int index = 1;

        for (Example cell : row.firstChild()) {
            if (hasAnnotation(cell)) {
                marked.add(index);
            }
            index ++ ;
        }
        return marked.toArray(new Integer[marked.size()]);
    }

    public static boolean isAnnotatedWith(Example example, Class< ? extends Annotation> annotationType) {
        return annotationType.isInstance(readAnnotation(example));
    }

    public static boolean hasAnnotation(Example example) {
        return readAnnotation(example) != null ? true : false;
    }

    public static String getAnnotationOnCell(Example example) {
        if (readAnnotation(example) == null) {
            return "none";
        }

        if (isAnnotatedWith(example, RightAnnotation.class)) {
            return "right";
        }
        if (isAnnotatedWith(example, WrongAnnotation.class)) {
            return "wrong";
        }
        if (isAnnotatedWith(example, IgnoredAnnotation.class)) {
            return "ignored";
        }
        if (isAnnotatedWith(example, ExceptionAnnotation.class)) {
            return "exception";
        }
        if (isAnnotatedWith(example, EnteredAnnotation.class)) {
            return "Entered";
        }
        if (isAnnotatedWith(example, NotEnteredAnnotation.class)) {
            return "Not entered";
        }
        if (isAnnotatedWith(example, SurplusAnnotation.class)) {
            return "surplus";
        }
        if (isAnnotatedWith(example, MissingAnnotation.class)) {
            return "missing";
        }
        if (isAnnotatedWith(example, SkippedAnnotation.class)) {
            return "Skipped";
        }

        return "unknown";
    }

    public static Annotation annotationFor(String content) {
        if (StringUtils.isBlank(content)) {
            return null;
        }

        if ("none".equals(content)) {
            return null;
        }

        if ("right".equals(content)) {
            return Annotations.right();
        }
        if ("wrong".equals(content)) {
            return Annotations.wrong();
        }
        if ("ignored".equals(content)) {
            return Annotations.ignored(null);
        }
        if ("error".equals(content)) {
            return Annotations.exception(null);
        }
        if ("entered".equals(content)) {
            return Annotations.entered();
        }
        if ("not entered".equals(content)) {
            return Annotations.notEntered();
        }
        if ("surplus".equals(content)) {
            return Annotations.surplus();
        }
        if ("missing".equals(content)) {
            return Annotations.missing();
        }
        if ("right".equals(content)) {
            return Annotations.right();
        }
        if ("skipped".equals(content)) {
            return Annotations.skipped();
        }

        return null;
    }

    private static Annotation readAnnotation(Example example) {
        return breachEncapsulation(example).getAnnotation();
    }

    private static BreachSpecificationEncapsulation breachEncapsulation(Example example) {
        if ( ! DuckType.instanceOf(BreachSpecificationEncapsulation.class, example)) {
            throw new IllegalArgumentException("Cant' breach encapsulation of " + example.getClass().getName());
        }
        return DuckType.implement(BreachSpecificationEncapsulation.class, example);
    }
}
