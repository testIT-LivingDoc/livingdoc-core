package info.novatec.testit.livingdoc;

/* would be nice to consider using a builder style, e.g. emphasize();
 * emphasizeEnd(); strong(); strongEnd(); ... horizontalLine(); newLine(); ... */
public interface Text {
    void setStyle(String property, String value);

    String getStyle(String property);

    void setContent(String content);

    String getContent();
}
