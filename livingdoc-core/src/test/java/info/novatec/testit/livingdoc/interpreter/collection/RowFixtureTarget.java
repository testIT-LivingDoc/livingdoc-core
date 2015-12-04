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
package info.novatec.testit.livingdoc.interpreter.collection;

public class RowFixtureTarget {
    public boolean queryCalled;
    private String a;

    public void setA(String a) {
        this.a = a;
    }

    public String getA() {
        return a;
    }

    public Object[] query() {
        TestObject[] objects = new TestObject[3];

        for (int i = 0; i != 3; ++ i) {
            objects[i] = new TestObject();
        }

        queryCalled = true;
        return objects;
    }

    public static class TestObject {
        public int getA() {
            return 1;
        }

        public int getB() {
            return 2;
        }

        public int getC() {
            return 3;
        }
    }
}
