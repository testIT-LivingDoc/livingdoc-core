package info.novatec.testit.livingdoc.server.rest.responses;

public class GetRenderedSpecificationResponse {

    private String specification;

    public GetRenderedSpecificationResponse() {
    }

    public GetRenderedSpecificationResponse(String specification) {
        this.specification = specification;
    }

    public String getSpecification() {
        return specification;
    }
}
