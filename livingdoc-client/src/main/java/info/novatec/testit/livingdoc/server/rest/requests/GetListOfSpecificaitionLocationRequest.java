package info.novatec.testit.livingdoc.server.rest.requests;


public class GetListOfSpecificaitionLocationRequest {

    String repoUID;
    String sut;

    public GetListOfSpecificaitionLocationRequest() {
    }

    public GetListOfSpecificaitionLocationRequest(String repoUID, String sut) {
        this.repoUID = repoUID;
        this.sut = sut;
    }
}
