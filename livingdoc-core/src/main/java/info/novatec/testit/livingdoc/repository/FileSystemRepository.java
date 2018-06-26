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

import info.novatec.testit.livingdoc.document.Document;
import info.novatec.testit.livingdoc.html.HtmlDocumentBuilder;
import info.novatec.testit.livingdoc.util.URIUtil;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;


public class FileSystemRepository implements DocumentRepository {
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
        throw new UnsupportedOperationException("Not supported");
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
        List<Object> hierarchy = toHierarchyNodeList(root);
        hierarchy.set(0, root.getName());
        hierarchy.set(1, false);
        hierarchy.set(2, false);
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

    private List<Object> toHierarchyNodeList(File file) {
        List<Object> myList = new ArrayList();
        myList.add(0, URIUtil.relativize(root.getAbsolutePath(), file.getAbsolutePath()));
        myList.add(1, ! file.isDirectory());
        myList.add(2, false);

        Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
        if (file.isDirectory() && file.listFiles() != null) {
            for (File node : file.listFiles(NOT_HIDDEN)) {
                hashtable.put(node.getName(), toHierarchyNodeList(node));
            }
        }

        myList.add(3, hashtable);
        return myList;
    }
}
