package info.novatec.testit.livingdoc.server.rest.requests;

/**
 * Created by FSU on 09.06.2017.
 */
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
