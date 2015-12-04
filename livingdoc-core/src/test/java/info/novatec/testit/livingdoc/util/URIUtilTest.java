package info.novatec.testit.livingdoc.util;

import static info.novatec.testit.livingdoc.util.CollectionUtil.joinAsString;
import static info.novatec.testit.livingdoc.util.CollectionUtil.toArray;
import static info.novatec.testit.livingdoc.util.URIUtil.decoded;
import static info.novatec.testit.livingdoc.util.URIUtil.flatten;
import static info.novatec.testit.livingdoc.util.URIUtil.raw;
import static info.novatec.testit.livingdoc.util.URIUtil.relativize;
import static info.novatec.testit.livingdoc.util.URIUtil.resolve;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URI;

import org.junit.Test;


public class URIUtilTest {

    @Test
    public void testRawEscapesSpaces() {
        assertEquals("/path/to/dir/sub%20dir/my%20file.html", raw("/path/to/dir/sub dir/my file.html"));
    }

    @Test
    public void testRawEscapesPercents() {
        assertEquals("/path/to/dir/sub%20dir/my%20file%2050%25.html", raw("/path/to/dir/sub dir/my file 50%.html"));
    }

    @Test
    public void testRawEscapesSingleQuotes() {
        assertEquals("/path/to/dir/sub%27dir/my%27file.html", raw("/path/to/dir/sub'dir/my'file.html"));
    }

    @Test
    public void testRawEscapesDoubleQuotes() {
        assertEquals("/path/to/dir/sub%22dir/my%22file.html", raw("/path/to/dir/sub\"dir/my\"file.html"));
    }

    @Test
    public void testRawNormalizesPathSeparators() {
        assertEquals("c:/path/to/my_file.html", raw(joinAsString(toArray("c:", "path", "to", "my_file.html"),
            File.separator)));
    }

    @Test
    public void testDecodedDecodesSpaces() {
        assertEquals("/path/to/dir/sub dir/my file.html", decoded("/path/to/dir/sub%20dir/my%20file.html"));
    }

    @Test
    public void testDecodedDecodesPercents() {
        assertEquals("/path/to/dir/sub dir/my file 50%.html", decoded("/path/to/dir/sub%20dir/my%20file%2050%25.html"));
    }

    @Test
    public void testDecodedNormalizesPathSeparators() {
        assertEquals("c:/path/to/my_file.html", decoded(joinAsString(toArray("c:", "path", "to", "my_file.html"),
            File.separator)));
    }

    @Test
    public void testFlatteningReplacesPathSeparatorsWithDashes() {
        assertEquals("path-to-my_file", flatten(joinAsString(toArray("path", "to", "my_file"), File.separator)));
    }

    @Test
    public void testFlatteningStripsLeadingPathSeparator() {
        assertEquals("path-to-file", flatten("/path/to/file"));
    }

    /* public void testFlatteningReplacesColonsWithUnderscores() { assertEquals(
     * "file_html", flatten( "file.html" )); } */

    @Test
    public void testCanComputeRelativePathFromBase() {
        assertEquals("subdir/file", relativize("c:/path/to/dir", "c:/path/to/dir/subdir/file"));
    }

    @Test
    public void testOperationsCanBeCombined() {
        String filePath = joinAsString(toArray("c:", "path", "to", "dir", "sub dir", "my file.html"), File.separator);
        String dirPath = joinAsString(toArray("c:", "path", "to"), File.separator);
        assertEquals("dir-sub dir-my file.html", flatten(relativize(dirPath, filePath)));
    }

    @Test
    public void testResolvesPathnamesFromBaseAndRelativePaths() {
        assertEquals("/base/child", resolve("/base", "child"));
        assertEquals("/base/child", resolve("/base/", "child"));
        assertEquals("/base/child", resolve("/base", "/child"));
        assertEquals("/base/child", resolve("/base/", "/child"));
    }

    @Test
    public void testWeCanRetrieveTheAttributesFromAnUri() {
        URI myUri = URI.create("http://domain:port/context/path?att1=attribute1&att2=attribute2&att3=attribute3");
        assertEquals("attribute1", URIUtil.getAttribute(myUri, "att1"));
        assertEquals("attribute2", URIUtil.getAttribute(myUri, "att2"));
        assertEquals("attribute3", URIUtil.getAttribute(myUri, "att3"));
    }

    @Test
    public void testEscapeFileSystemForbiddenCharacters_DoubleQuote() {
        assertEquals("%22quote%22", URIUtil.escapeFileSystemForbiddenCharacters("\"quote\""));
    }

    @Test
    public void testEscapeFileSystemForbiddenCharacters_Pipe() {
        assertEquals("%7Cpipe%7C", URIUtil.escapeFileSystemForbiddenCharacters("|pipe|"));
    }

    @Test
    public void testEscapeFileSystemForbiddenCharacters_InterogativePoint() {
        assertEquals("%3Fquestion%3F", URIUtil.escapeFileSystemForbiddenCharacters("?question?"));
    }

    @Test
    public void testEscapeFileSystemForbiddenCharacters_SuperiorSign() {
        assertEquals("%3Esup%3E", URIUtil.escapeFileSystemForbiddenCharacters(">sup>"));
    }

    @Test
    public void testEscapeFileSystemForbiddenCharacters_InferiorSign() {
        assertEquals("%3Cinf%3C", URIUtil.escapeFileSystemForbiddenCharacters("<inf<"));
    }
}
