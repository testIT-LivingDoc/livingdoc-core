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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.document.Document;
import info.novatec.testit.livingdoc.util.TestFileUtils;


public class FileSystemRepositoryTest {
    private File root, hierarchy;
    private FileSystemRepository repository, hierarchyRepo;

    @Before
    public void setUp() throws Exception {
        String tempDirPath = FileUtils.getTempDirectory().getAbsolutePath();
        root = TestFileUtils.createTempDirectory("testfsr");
        root.mkdir();
        hierarchy = new File("hierarchy", tempDirPath);
        hierarchy.mkdir();
        repository = new FileSystemRepository(root);
        hierarchyRepo = new FileSystemRepository(hierarchy);
    }

    @After
    public void tearDown() throws Exception {
        deleteOutputDirectory();
    }

    private void deleteOutputDirectory() {
        if (root != null) {
            FileUtils.deleteQuietly(root);
        }
    }

    @Test(expected = DocumentNotFoundException.class)
    public void testComplainsIfDocumentIsNotFound() throws Exception {
        repository.loadDocument("unknown.file");
    }

    @Test
    public void testLocatesAndLoadsHtmlFiles() throws Exception {
        Document doc = repository.loadDocument(createDocument(root, "/subdir/specification.html"));
        assertSpecification(doc);
    }

    @Test
    public void testShouldListAllVisibleHtmlFilesInDirectory() throws Exception {
        List<String> specFiles = createSpecificationFiles(root);
        createOtherFiles(root);
        Set<String> listing = new HashSet<String>(repository.listDocuments("/specs"));

        assertTrue(listing.containsAll(specFiles));
        assertTrue(specFiles.containsAll(listing));
    }

    @Test
    public void testShouldBeAbleToLoadListedFiles() throws Exception {
        createDocument(root, "/directory/file.html");
        createDocument(root, "/directory/sub directory/some file.html");
        createDocument(root, "/directory/file with uppercase ext.HTML");
        List<String> docs = repository.listDocuments("directory");
        assertEquals(3, docs.size());
        for (String doc : docs) {
            assertSpecification(repository.loadDocument(doc));
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testWeCanRetrieveTheDocumentsInAHierarchy() throws Exception {
        List<String> names = Arrays.asList(new String[] { "specs", "specs/dir1", "specs/dir1/spec1.html",
                "specs/dir1/subdir1", "specs/dir1/subdir1/spec2.html", "specs/dir1/subdir1/spec4.html", "specs/dir2",
                "spec3.html" });
        createSpecificationHierarchyFiles(hierarchy, names);
        List<Object> hierarchies = hierarchyRepo.listDocumentsInHierarchy();
        assertNamesInHierarchy((Hashtable<Object, Object>) hierarchies.get(3), names);
    }

    @Test
    public void testAFileShouldNotHoldAnyDocument() throws Exception {

        List<String> listing = repository.listDocuments(createDocument(root, "test.html"));
        assertTrue(listing.isEmpty());
    }

    private List<String> createOtherFiles(File rootFile) throws Exception {
        return Arrays.asList(createDocument(rootFile, "/specs/dir1/not_a_spec.log"), createDocument(rootFile,
            "/specs/dir1/dir3/a_text_file.txt"));
    }

    private List<String> createSpecificationFiles(File rootFile) throws Exception {
        return Arrays.asList(createDocument(rootFile, "/specs/dir1/spec1.html"), createDocument(rootFile,
            "/specs/dir1/spec2.html"), createDocument(rootFile, "/specs/dir1/dir3/spec4.html"), createDocument(rootFile,
                "/specs/dir2/spec3.html"));
    }

    private void createSpecificationHierarchyFiles(File rootFile, List<String> names) throws Exception {
        for (String name : names) {
            createDocument(rootFile, name);
        }
    }


    @SuppressWarnings("unchecked")
    private void assertNamesInHierarchy(Hashtable<Object, Object> branch, List<String> names) {
        Iterator<Object> iter = branch.keySet().iterator();
        while (iter.hasNext()) {
            String name = ( String ) iter.next();
            List<Object> child = (ArrayList<Object>) branch.get(name);
            assertTrue(names.contains(child.get(0)));
            assertNamesInHierarchy(( Hashtable<Object, Object> ) child.get(3), names);
        }
    }

    private void assertSpecification(Document doc) {
        assertNotNull(doc);

        StringWriter buffer = new StringWriter();
        PrintWriter writer = new PrintWriter(buffer);
        doc.print(writer);
        writer.close();

        assertEquals(specificationhtml(), buffer.toString());
    }

    private String createDocument(File rootFile, String fileName) {

        File file = new File(rootFile, fileName);
        Writer writer = null;
        file.getParentFile().mkdirs();
        try {
            if (fileName.toLowerCase().endsWith(".html")) {

                writer = new FileWriter(file);
                writer.write(specificationhtml());
                writer.flush();
            }
        } catch (IOException e) {
            IOUtils.closeQuietly(writer);
        }
        return fileName;
    }

    private String specificationhtml() {
        return "<html><table border='1' cellspacing='0'>" + "<tr><td colspan='3'>My Fixture</td></tr>"
            + "<tr><td>a</td><td>b</td><td>sum()</td></tr>" + "<tr><td>1</td><td>2</td><td>3</td></tr>"
            + "<tr><td>2</td><td>3</td><td>15</td></tr>" + "<tr><td>2</td><td>3</td><td>a</td></tr>" + "</table></html>";
    }
}
