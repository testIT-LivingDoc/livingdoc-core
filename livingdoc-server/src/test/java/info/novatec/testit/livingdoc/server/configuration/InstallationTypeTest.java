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
package info.novatec.testit.livingdoc.server.configuration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class InstallationTypeTest {
    @Test
    public void testCanRetrieveEnumById() {
        assertEquals(InstallationType.quick, InstallationType.fromId("quick"));
        assertEquals(InstallationType.quick, InstallationType.fromId("QuIcK"));
        assertEquals(InstallationType.custom, InstallationType.fromId("custom"));
        assertEquals("Custom", InstallationType.fromId("CustoM").getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCannotRetrieveEnumByBadId() {
        InstallationType.fromId("!bad!");
    }
}
