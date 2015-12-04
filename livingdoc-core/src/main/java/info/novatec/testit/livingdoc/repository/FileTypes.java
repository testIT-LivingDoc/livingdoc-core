package info.novatec.testit.livingdoc.repository;

public enum FileTypes {
    HTML ( "html" ), MARKUP ( "markup" ), CONFLUENCE ( "confluence" ), NOTSUPPORTED ( "nosup" );

    private String fileExtension;

    FileTypes(String fext) {
        fileExtension = fext;
    }

    public String returnExtension() {
        return fileExtension;
    }
}
