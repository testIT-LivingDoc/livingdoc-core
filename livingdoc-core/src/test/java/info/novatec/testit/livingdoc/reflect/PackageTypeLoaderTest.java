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

package info.novatec.testit.livingdoc.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;


public class PackageTypeLoaderTest {
    private PackageTypeLoader< ? > loader = new PackageTypeLoader<Object>(new JavaTypeLoader<Object>(Object.class));

    @Test
    public void testUsesPackagesToCompleteTypeName() throws Exception {
        loader.searchPackage("info.novatec.testit.livingdoc");
        assertNotNull(loader.loadType("Calculator"));
    }

    @Test
    public void testPackagesAddedLastAreConsideredFirst() throws Exception {
        loader.searchPackage("info.novatec.testit.livingdoc.reflect.override");
        loader.searchPackage("info.novatec.testit.livingdoc.reflect.masked");
        loader.searchPackage("info.novatec.testit.livingdoc.reflect.override");
        assertEquals(info.novatec.testit.livingdoc.reflect.override.Fixture.class, loader.loadType("Fixture")
            .getUnderlyingClass());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testPackagesAddedMustBeUnique() throws Exception {
        TypeLoader<Object> typeLoaderMock = mock(TypeLoader.class);

        when(typeLoaderMock.loadType("Calculator")).thenReturn(null);
        when(typeLoaderMock.loadType("info.novatec.testit.livingdoc.Calculator")).thenReturn(null);

        PackageTypeLoader< ? > packageTypeLoader = new PackageTypeLoader<Object>(typeLoaderMock);

        packageTypeLoader.searchPackage("info.novatec.testit.livingdoc");
        packageTypeLoader.searchPackage("info.novatec.testit.livingdoc");
        assertNull(packageTypeLoader.loadType("Calculator"));

        verify(typeLoaderMock).loadType("Calculator");
        verify(typeLoaderMock).loadType("info.novatec.testit.livingdoc.Calculator");
        verify(typeLoaderMock).loadType("info.novatec.testit.livingdoc$Calculator");
        verifyNoMoreInteractions(typeLoaderMock);
    }
}
