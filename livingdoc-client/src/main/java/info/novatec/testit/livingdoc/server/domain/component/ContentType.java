package info.novatec.testit.livingdoc.server.domain.component;

import javax.persistence.Embeddable;

@Embeddable
public class ContentType {

    public static final ContentType TEST = new ContentType("TEST");
    public static final ContentType REQUIREMENT = new ContentType("REQUIREMENT");
    public static final ContentType BOTH = new ContentType("BOTH");
    public static final ContentType UNKNOWN = new ContentType("UNKNOWN");

    private String contentType;

    private ContentType() {
    }

    private ContentType(String contentType) {
        this.contentType = contentType;
    }

    public static ContentType getInstance(String type) {
        if (type.equals(TEST.toString())) {
            return TEST;
        } else if (type.equals(REQUIREMENT.toString())) {
            return REQUIREMENT;
        } else if (type.equals(BOTH.toString())) {
            return BOTH;
        }

        return UNKNOWN;
    }

    public static boolean containsSpecifications(ContentType type) {
        return containsSpecifications(type.getContentType());
    }

    public static boolean containsSpecifications(String type) {
        return type.equals(TEST.toString()) || type.equals(BOTH.toString());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ContentType && getContentType().equals(((ContentType) obj).getContentType());
    }

    @Override
    public int hashCode() {
        return getContentType().hashCode();
    }

    @Override
    public String toString() {
        return this.contentType;
    }

    private String getContentType() {
        return this.contentType;
    }

    @SuppressWarnings("unused")
    private void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
