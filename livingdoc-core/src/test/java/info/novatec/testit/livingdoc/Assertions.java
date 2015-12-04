/* Copyright (c) 2006 Pyxis Technologies inc.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org. */

package info.novatec.testit.livingdoc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import info.novatec.testit.livingdoc.annotation.Annotation;
import info.novatec.testit.livingdoc.annotation.EnteredAnnotation;
import info.novatec.testit.livingdoc.annotation.ExceptionAnnotation;
import info.novatec.testit.livingdoc.annotation.IgnoredAnnotation;
import info.novatec.testit.livingdoc.annotation.MissingAnnotation;
import info.novatec.testit.livingdoc.annotation.RightAnnotation;
import info.novatec.testit.livingdoc.annotation.SkippedAnnotation;
import info.novatec.testit.livingdoc.annotation.StoppedAnnotation;
import info.novatec.testit.livingdoc.annotation.SurplusAnnotation;
import info.novatec.testit.livingdoc.annotation.WrongAnnotation;
import info.novatec.testit.livingdoc.util.BreachSpecificationEncapsulation;
import info.novatec.testit.livingdoc.util.DuckType;


public class Assertions {
    private Assertions() {
    }

    public static void assertNotAnnotated(Example example) {
        assertNull(String.valueOf(readAnnotation(example)), readAnnotation(example));
    }

    public static void assertAnnotatedRight(Example example) {
        assertAnnotatedWith(example, RightAnnotation.class);
    }

    public static void assertAnnotatedWrongWithoutDetail(Example example) {
        assertAnnotatedWrong(example);
        assertFalse( ( ( WrongAnnotation ) readAnnotation(example) ).isDetailed());
    }

    public static void assertAnnotatedWrong(Example example) {
        assertAnnotatedWith(example, WrongAnnotation.class);
    }

    public static void assertAnnotatedException(Example example) {
        assertAnnotatedWith(example, ExceptionAnnotation.class);
    }

    public static void assertAnnotatedIgnored(Example example) {
        assertAnnotatedWith(example, IgnoredAnnotation.class);
    }

    public static void assertAnnotatedMissing(Example example) {
        assertAnnotatedWith(example, MissingAnnotation.class);
    }

    public static void assertAnnotatedSurplus(Example example) {
        assertAnnotatedWith(example, SurplusAnnotation.class);
    }

    public static void assertAnnotatedEntered(Example example) {
        assertAnnotatedWith(example, EnteredAnnotation.class);
    }

    public static void assertAnnotatedSkipped(Example example) {
        assertAnnotatedWith(example, SkippedAnnotation.class);
    }

    public static void assertAnnotatedStopped(Example example) {
        assertAnnotatedWith(example, StoppedAnnotation.class);
    }

    public static void assertAnnotatedWrongWithDetails(Example example) {
        assertAnnotatedWrong(example);
        assertTrue( ( ( WrongAnnotation ) readAnnotation(example) ).isDetailed());
    }

    public static void assertAnnotatedWith(Example example, Class< ? extends Annotation> annotationType) {
        assertTrue(String.valueOf(readAnnotation(example)), annotationType.isInstance(readAnnotation(example)));
    }

    public static Annotation readAnnotation(Example example) {
        return breachEncapsulation(example).getAnnotation();
    }

    private static BreachSpecificationEncapsulation breachEncapsulation(Example example) {
        if ( ! DuckType.instanceOf(BreachSpecificationEncapsulation.class, example)) {
            throw new IllegalArgumentException("Cant' breach encapsulation of " + example.getClass().getName());
        }
        return DuckType.implement(BreachSpecificationEncapsulation.class, example);
    }
}
