package info.novatec.testit.livingdoc.util;

import java.io.File;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;


public final class URIUtil {
    private URIUtil() {
    }

    /**
     * A basic quoting implementation. It escapes path separators and spaces. If
     * a more robust solution is required look at {@link java.net.URI} or
     * org.apache.commons.httpclient.URI.
     */
    public static String raw(String uri) {
        String quoted = uri.replaceAll("\\%", "%25");
        quoted = quoted.replaceAll("\\s", "%20");
        quoted = quoted.replaceAll("\\\"", "%22");
        quoted = quoted.replaceAll("\\'", "%27");
        quoted = quoted.replaceAll("\\" + File.separator, "/");
        return quoted;
    }

    public static String decoded(String uri) {
        String decoded = uri.replaceAll("%20", " ");
        decoded = decoded.replaceAll("%22", "\"");
        decoded = decoded.replaceAll("%27", "'");
        decoded = decoded.replaceAll("\\s", " ");
        decoded = decoded.replaceAll("%25", "%");
        decoded = decoded.replaceAll("\\" + File.separator, "/");
        return decoded;
    }

    public static String flatten(String uri) {
        URI normalized = URI.create(raw(uri)).normalize();
        String path = normalized.getPath();
        path = stripLeadingSlash(path);
        path = path.replaceAll("/", "-");
        return path;
    }

    public static String relativize(String base, String uri) {
        URI child = URI.create(raw(uri));
        URI parent = URI.create(raw(base));
        return parent.relativize(child).getPath();
    }

    public static String resolve(String base, String child) {
        String childPath = child.startsWith("/") ? child.substring(1) : child;
        String basePath = base.endsWith("/") ? base : base + "/";
        return basePath + childPath;
    }

    public static String getAttribute(URI uri, String attributeName) {
        String query = uri.getQuery();
        if (StringUtils.isEmpty(query)) {
            return null;
        }

        Pattern pattern = Pattern.compile(attributeName + "\\=([^&]*)");
        Matcher matcher = pattern.matcher(query);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static String stripLeadingSlash(String path) {
        return path.startsWith("/") ? path.substring(1) : path;
    }

    public static String escapeFileSystemForbiddenCharacters(String input) {
        return input.replace("?", "%3F").replace(">", "%3E").replace("<", "%3C").replace("\"", "%22").replace("|", "%7C");
    }
}
