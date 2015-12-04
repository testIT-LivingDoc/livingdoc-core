package info.novatec.testit.livingdoc.util;

public class HtmlUtil {
    public static String cleanUpResults(String resultsToClean) {
        if (resultsToClean != null) {
            String cleanedString = resultsToClean.replace("</html><html>", "<br/><br/>");
            cleanedString = cleanedString.replace("</HTML><HTML>", "<br/><br/>");
            cleanedString = cleanedString.replace("<html>", "");
            cleanedString = cleanedString.replace("</html>", "");
            cleanedString = cleanedString.replace("<HTML>", "");
            cleanedString = cleanedString.replace("</HTML>", "");
            cleanedString = cleanedString.replace("<body>", "");
            cleanedString = cleanedString.replace("</body>", "");
            cleanedString = cleanedString.replace("<BODY>", "");
            cleanedString = cleanedString.replace("</BODY>", "");
            return cleanedString;
        }

        return null;
    }
}
