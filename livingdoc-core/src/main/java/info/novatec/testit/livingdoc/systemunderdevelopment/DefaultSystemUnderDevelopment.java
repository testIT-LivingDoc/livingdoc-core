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

package info.novatec.testit.livingdoc.systemunderdevelopment;

import java.util.HashSet;
import java.util.Set;

import info.novatec.testit.livingdoc.document.Document;
import info.novatec.testit.livingdoc.reflect.AnnotationLoader;
import info.novatec.testit.livingdoc.reflect.DefaultFixture;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.Type;
import info.novatec.testit.livingdoc.reflect.TypeLoader;
import info.novatec.testit.livingdoc.reflect.TypeNotFoundException;


public class DefaultSystemUnderDevelopment implements SystemUnderDevelopment {
    private TypeLoader< ? > typeLoader;
    private AnnotationLoader< ? > altLoader;
    private Set<String> importedPackages = new HashSet<String>();
    private boolean hasImports = false;
    private ClassLoader classLoader;

    public DefaultSystemUnderDevelopment() {
        ClassLoader currentClassLoader = this.getClass().getClassLoader();
        setClassLoader(currentClassLoader);
    }

    public DefaultSystemUnderDevelopment(java.lang.ClassLoader classLoader) {
        setClassLoader(classLoader);
    }

    public DefaultSystemUnderDevelopment(TypeLoader< ? > typeLoader) {
        this.typeLoader = typeLoader;
    }

    /**
     * Creates a new instance of a fixture class using a set of parameters.
     * 
     * @param name the name of the class to instantiate
     * @param params the parameters (constructor arguments)
     * @return a new instance of the fixtureClass with fields populated using
     * Constructor
     */
    @Override
    public Fixture getFixture(String name, String... params) throws Throwable {
        Type< ? > type = loadType(name);
        Object target = type.newInstanceUsingCoercion(params);
        return new DefaultFixture(target);
    }

    protected Type< ? > loadType(String name) {
        Type< ? > type = typeLoader.loadType(name);
        if (type == null && ! hasImports) {
            initAltLoader();
            type = altLoader.getAnnotatedFixture(name);
        }
        if (type == null) {
            throw new TypeNotFoundException(name);
        }
        return type;
    }

    @Override
    public void addImport(String packageName) {
        typeLoader.searchPackage(packageName);
        // Store the imported packages to be able to use a new class loader.
        this.importedPackages.add(packageName);
        hasImports = true;
    }

    @Override
    public void onEndDocument(Document document) {
        // No implementation needed.
    }

    @Override
    public void onStartDocument(Document document) {
        // No implementation needed.
    }

    @Override
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        initializeTypeLoader();
    }

    /**
     * If we reset the class loader, we would loose all previous added imports.
     * Therefore we've to add them again.
     */
    private void initializeTypeLoader() {
        this.typeLoader = new FixtureTypeLoaderChain(classLoader);
        if (altLoader != null) {
            altLoader.addLoader(classLoader);
        }
        for (String importedPackage : importedPackages) {
            typeLoader.searchPackage(importedPackage);
        }
    }

    private void initAltLoader() {
        if (altLoader == null) {
            altLoader = AnnotationLoader.INSTANCE;
            altLoader.addLoader(classLoader);
        }
    }

    public void setHasImports(boolean hasImports) {
        this.hasImports = hasImports;
    }
}
