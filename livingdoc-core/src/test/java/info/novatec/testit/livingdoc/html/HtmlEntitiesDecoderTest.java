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
package info.novatec.testit.livingdoc.html;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class HtmlEntitiesDecoderTest {

    @Test
    public void testShouldProperlyDecodeEntities() {
        assertEquals("", decode(""));
        assertEquals("<", decode("&lt;"));
        assertEquals(">", decode("&gt;"));
        assertEquals("&", decode("&amp;"));
        assertEquals("\n", decode("<br/>"));
        assertEquals("\u00e7", decode("&ccedil;"));
        assertEquals("\u00e7", decode("&#xe7;"));
        assertEquals("\u00e7", decode("&#Xe7;"));
        assertEquals("Ben & Jerry", decode("Ben & Jerry"));
        assertEquals("Mickey & Minnie & Friends", decode("Mickey & Minnie & Friends"));
        assertEquals("& ;", decode("& ;"));
        assertEquals("&;", decode("&;"));
        assertEquals("&&;", decode("&&;"));
        assertEquals("ch\u00e8que", decode("ch&#232;que"));
        assertEquals("fran\u00e7ais", decode("fran&#231;ais"));
        assertEquals("& a long sentence;", decode("& a long sentence;"));
    }

    private String decode(String value) {
        return new HtmlEntitiesDecoder(value).decode();
    }
}
