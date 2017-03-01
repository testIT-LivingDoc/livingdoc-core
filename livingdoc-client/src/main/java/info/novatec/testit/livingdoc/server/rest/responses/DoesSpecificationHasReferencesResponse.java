package info.novatec.testit.livingdoc.server.rest.responses;

public class DoesSpecificationHasReferencesResponse {
    public boolean hasReferences;

    public DoesSpecificationHasReferencesResponse() {
    }

    public DoesSpecificationHasReferencesResponse(boolean hasReferences) {
        this.hasReferences = hasReferences;
    }

    public boolean hasReferences() {
        return hasReferences;
    }
}
