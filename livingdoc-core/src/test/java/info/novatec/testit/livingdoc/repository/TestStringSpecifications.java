package info.novatec.testit.livingdoc.repository;

public class TestStringSpecifications {

    public static final String SimpleAlternateCalculatorTest = "<html><table border='1' cellspacing='0'>"
        + "<tr><td colspan='2'>info.novatec.testit.livingdoc.testing.AlternateCalculator</td></tr>"
        + "<tr><td>a</td><td>b</td><td>sum()</td></tr>" + "<tr><td>1</td><td>2</td><td>3</td></tr>"
        + "<tr><td>2</td><td>3</td><td>15</td></tr>" + "<tr><td>2</td><td>3</td><td>a</td></tr>" + "</table></html>";

    public static final String SimpleAlternateCalculatorXmlReport =
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + "<documents><document>"
            + "<time-statistics><execution>0</execution><total>0</total></time-statistics>"
            + "<statistics><success>0</success><failure>0</failure><error>0</error><ignored>0</ignored></statistics>"
            + "<results>"
            + "<![CDATA[<html><table border='1' cellspacing='0'><tr><td colspan='2'>info.novatec.testit.livingdoc.testing.AlternateCalculator</td></tr>"
            + "<tr><td>a</td><td>b</td><td>sum()</td></tr><tr><td>1</td><td>2</td><td>3</td></tr><tr><td>2</td>"
            + "<td>3</td><td>15</td></tr><tr><td>2</td><td>3</td><td>a</td></tr></table></html>]]>"
            + "</results></document></documents>";
}
