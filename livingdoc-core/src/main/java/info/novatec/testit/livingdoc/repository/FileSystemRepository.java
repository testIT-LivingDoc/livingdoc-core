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
package info.novatec.testit.livingdoc.repository;

import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.document.Document;
import info.novatec.testit.livingdoc.html.HtmlDocumentBuilder;
import info.novatec.testit.livingdoc.util.URIUtil;


public class FileSystemRepository implements DocumentRepository {
    private static final Logger LOG = LoggerFactory.getLogger(FileSystemRepository.class);

    private static final FileFilter NOT_HIDDEN = new NotHiddenFilter();
    private final File root;

    public FileSystemRepository(String... args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("root");
        }
        this.root = new File(URIUtil.decoded(args[0]));
    }

    public FileSystemRepository(File root) {
        this.root = root;
    }

    @Override
    public void setDocumentAsImplemented(String location) {
        // No implementation needed.
    }

    @Override
    public List<String> listDocuments(String location) throws IOException {
        File parent = fileAt(location);
        if ( ! parent.exists()) {
            return Collections.emptyList();
        }

        List<String> names = new ArrayList<String>();
        if (parent.isDirectory()) {
            for (File child : parent.listFiles(NOT_HIDDEN)) {
                if (child.isDirectory()) {
                    names.addAll(listDocuments(relativePath(child)));
                } else if ( ! checkFileType(child).equals(FileTypes.NOTSUPPORTED)) {
                    names.add(relativePath(child));
                }
            }
        }
        return names;
    }

    @Override
    public List<Object> listDocumentsInHierarchy() {
        Vector<Object> hierarchy = toHierarchyNodeVector(root);
        hierarchy.setElementAt(root.getName(), 0);
        hierarchy.setElementAt(false, 1);
        hierarchy.setElementAt(false, 2);
        return hierarchy;
    }

    @Override
    public Document loadDocument(String location) throws IOException, DocumentNotFoundException,
        UnsupportedDocumentException {
        File file = fileAt(location);
        if ( ! file.exists()) {
            throw new DocumentNotFoundException(file.getAbsolutePath());
        }
        switch (checkFileType(file)) {
            case HTML:
                return loadHtmlDocument(file);
            default:
                throw new UnsupportedDocumentException(location);
        }
    }

    private File fileAt(String location) {
        return new File(root, location);
    }

    private String relativePath(File file) throws IOException {
        return normalizedPath(file).substring(normalizedPath(root).length());
    }

    private String normalizedPath(File file) throws IOException {
        return file.getCanonicalPath().replaceAll("\\" + File.separator, "/");
    }

    private Document loadHtmlDocument(File file) throws IOException {
        Reader reader = new FileReader(file);
        try {
            return HtmlDocumentBuilder.tablesAndLists().build(reader);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    private static class NotHiddenFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return ! pathname.isHidden();
        }
    }

    private FileTypes checkFileType(File pathname) {
        for (FileTypes fileType : FileTypes.values()) {
            if (pathname.getAbsolutePath().toLowerCase().endsWith("." + fileType.returnExtension())) {
                return fileType;
            }
        }
        return FileTypes.NOTSUPPORTED;
    }

    private Vector<Object> toHierarchyNodeVector(File file) {
        Vector<Object> vector = new Vector<Object>();
        vector.add(0, URIUtil.relativize(root.getAbsolutePath(), file.getAbsolutePath()));
        vector.add(1, ! file.isDirectory());
        vector.add(2, false);

        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
        if (file.isDirectory() && file.listFiles() != null) {
            for (File node : file.listFiles(NOT_HIDDEN)) {
                try {
                    hashtable.put(node.getName(), toHierarchyNodeVector(node));
                } catch (Exception e) {
                    LOG.error(LOG_ERROR, e);
                    // URI not standard skip it !
                }
            }
        }

        vector.add(3, hashtable);
        return vector;
    }
}
