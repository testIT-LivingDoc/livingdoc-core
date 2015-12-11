/**
 * Copyright (c) 2009 Pyxis Technologies inc.
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
 * http://www.fsf.org.
 */
package info.novatec.testit.livingdoc.interpreter.flow.scenario;

import info.novatec.testit.livingdoc.Annotatable;
import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.call.Stub;


final class Annotate {

    private Annotate() {}

    public static Stub display(Example example) {
        return new AnnotateDisplay(example);
    }

    public static Stub given(Annotatable annotable, ScenarioMessage message, Statistics stats) {
        return new AnnotateGiven(annotable, message, stats);
    }

    public static Stub then(Annotatable annotable, ScenarioMessage message, Statistics stats) {
        return new AnnotateThen(annotable, message, stats);
    }

    public static Stub when(Annotatable annotable, Statistics stats) {
        return new AnnotateWhen(annotable, stats);
    }

    public static Stub check(Annotatable annotatable, Statistics stats) {
        return new AnnotateCheck(annotatable, stats);
    }
}
