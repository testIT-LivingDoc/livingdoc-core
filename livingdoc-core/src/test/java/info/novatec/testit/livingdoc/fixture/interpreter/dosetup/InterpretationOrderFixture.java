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
package info.novatec.testit.livingdoc.fixture.interpreter.dosetup;

import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


@FixtureClass("DoSetup Interpretation Order")
public class InterpretationOrderFixture extends
    info.novatec.testit.livingdoc.fixture.interpreter.flow.dowith.InterpretationOrderDoWithFixture {
    /**
     * This fixture is working as the DoWith Interpretation Order Fixture but
     * need to have it under is own package (conflicting with Setup and DoWith
     * seeds)
     */
}
