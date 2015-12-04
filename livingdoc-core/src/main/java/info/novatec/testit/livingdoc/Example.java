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

import java.io.PrintWriter;


public interface Example extends Annotatable, Iterable<Example> {

    String getContent();

    boolean hasChild();

    Example firstChild();

    boolean hasSibling();

    Example nextSibling();

    /**
     * Returns the last sibling of this example or this example if it has no
     * sibling.
     * 
     * @return the last sibling
     */
    Example lastSibling();

    int remainings();

    /**
     * returns the i(th) sibling.
     * <p/>
     * Note: at(0) returns this.<br/>
     * at(1) returns nextSibling().<br/>
     * at(2) returns nextSibling().nextSibling().<br/>
     * at(n) returns nextSibling()...nextSibling()...nextSibling().<br/>
     * <p/>
     * 
     * @param i the position
     * @return the i(th) sibling
     */
    Example at(int i);

    /**
     * Retrieves a descendant.
     * <p/>
     * Note: at(0, 0) returns firstChild().<br/>
     * at(0, 1) returns firstChild().nextSibling().<br/>
     * at(0, 2) returns firstChild().nextSibling().nextSibling().<br/>
     * at(0, n) returns
     * firstChild().nextSibling()...nextSibling()...nextSibling().<br/>
     * at(1, 0) returns nextSibling().firstChild().<br/>
     * at(2, 0) returns nextSibling().nextSibling().firstChild().<br/>
     * at(n, 0) returns
     * nextSibling()...nextSibling()...nextSibling().firstChild().<br/>
     * at(n, n) returns
     * nextSibling()...nextSibling()...nextSibling().firstChild(
     * ).nextSibling()...nextSibling()...nextSibling().<br/>
     * at(1, 1, 1) returns
     * nextSibling().firstChild().nextSibling().firstChild().nextSibling().<br/>
     * at(2, 1, 1, 2) returns
     * nextSibling().nextSibling().firstChild().nextSibling
     * ().firstChild().nextSibling().firstChild().nextSibling().nextSibling().
     * <br/>
     * ...<br/>
     * <p/>
     * 
     * @param i the position
     * @param positions additional positions
     * @return the descendant of the i(th) sibling
     */
    Example at(int i, int... positions);

    /**
     * Returns the remaining number of siblings (Self-included).
     */

    /**
     * Recursively prints this example and its children.
     * 
     * @param out The print object
     */
    void print(PrintWriter out);

    void setContent(String content);

    /**
     * Adds a new sibling at the end of the siblings.
     * <p/>
     * 
     * @return the newly added sibling
     */
    Example addSibling();

    /**
     * Adds a new child at the end of the childs.
     * <p/>
     * 
     * @return the newly added child
     */
    Example addChild();
}
