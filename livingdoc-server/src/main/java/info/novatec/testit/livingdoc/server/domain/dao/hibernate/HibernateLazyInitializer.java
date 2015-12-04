/* Copyright (c) 2008 Pyxis Technologies inc.
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
package info.novatec.testit.livingdoc.server.domain.dao.hibernate;

import java.util.Collection;

import info.novatec.testit.livingdoc.server.domain.Marshalizable;


/**
 * This class is a temporary patch to make sure all objects (including depth)
 * are all initialized by Hibernate. We try to add FetchType.EAGER for
 * associations without success.
 * 
 * To accomplish this, we use the same code from the XML-RPC side, ie, calling
 * the Marshallize method on object.
 */
public class HibernateLazyInitializer {
    public static void init(Object object) {
        if (object instanceof Collection) {
            initCollection(( Collection< ? > ) object);
        } else if (object instanceof Marshalizable) {
            Marshalizable m = ( Marshalizable ) object;
            m.marshallize();
        }
    }

    public static void initCollection(Collection< ? > collection) {
        for (Object object : collection) {
            init(object);
        }
    }
}
