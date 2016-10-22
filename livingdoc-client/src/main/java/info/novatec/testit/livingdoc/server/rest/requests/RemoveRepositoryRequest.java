package info.novatec.testit.livingdoc.server.rest.requests;

public class RemoveRepositoryRequest {
    public String repositoryUid;

    public RemoveRepositoryRequest() {
    }

    public RemoveRepositoryRequest(String repositoryUid) {
        this.repositoryUid = repositoryUid;
    }

}
