/* Copyright (c) 2007 Pyxis Technologies inc.
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

package info.novatec.testit.livingdoc.maven.plugin;

import static info.novatec.testit.livingdoc.util.CollectionUtil.toArray;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import info.novatec.testit.livingdoc.repository.DocumentRepository;


public class Repository {
    public final List<String> suites;
    public final List<String> tests;
    private String type;
    private String root;
    private String name;

    private DocumentRepository documentRepository;

    public Repository() {
        suites = new ArrayList<String>();
        tests = new ArrayList<String>();
    }

    public void addTest(String uri) {
        tests.add(uri);
    }

    public void addSuite(String uri) {
        suites.add(uri);
    }

    public String[] getTests() {
        return toArray(tests);
    }

    public String[] getSuites() {
        return toArray(suites);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DocumentRepository newInstance() throws IllegalArgumentException, InstantiationException, IllegalAccessException,
        InvocationTargetException, ClassNotFoundException, SecurityException, NoSuchMethodException {
        Class< ? > klass = Class.forName(type);
        if ( ! DocumentRepository.class.isAssignableFrom(klass)) {
            throw new IllegalArgumentException("Not a " + DocumentRepository.class.getName() + ": " + type);
        }

        Constructor< ? > constructor = klass.getConstructor(String[].class);
        return ( DocumentRepository ) constructor.newInstance(new Object[] { new String[] { root } });
    }

    public DocumentRepository getDocumentRepository() throws SecurityException, IllegalArgumentException,
        InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException,
        NoSuchMethodException {
        if (documentRepository == null) {
            documentRepository = newInstance();
        }
        return documentRepository;
    }
}
