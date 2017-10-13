package info.novatec.testit.livingdoc.server.transfer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import info.novatec.testit.livingdoc.server.domain.Marshalizable;


/**
 * Pojo mapping specification location data to the {@link List} format
 * supported by XML RPC.
 */
@SuppressWarnings("serial")
public class SpecificationLocation implements Serializable, Marshalizable {

    public final static int REPOSITORY_TYPE_CLASSNAME_IDX = 0;

    public final static int BASE_TEST_URL_IDX = 1;

    public final static int USERNAME_IDX = 2;

    public final static int PASSWORD_IDX = 3;

    public final static int SPEC_NAME_IDX = 4;

    private String repositoryTypeClassName;
    private String baseTestUrl;
    private String username;
    private String password;
    private String specificationName;
    public String getRepositoryTypeClassName() {
        return repositoryTypeClassName;
    }
    public void setRepositoryTypeClassName(String repositoryTypeClassName) {
        this.repositoryTypeClassName = repositoryTypeClassName;
    }
    public String getBaseTestUrl() {
        return baseTestUrl;
    }
    public void setBaseTestUrl(String baseTestUrl) {
        this.baseTestUrl = baseTestUrl;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getSpecificationName() {
        return specificationName;
    }
    public void setSpecificationName(String specificationName) {
        this.specificationName = specificationName;
    }
    @Override
    public List<Object> marshallize() {
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(REPOSITORY_TYPE_CLASSNAME_IDX,repositoryTypeClassName);
        parameters.add(BASE_TEST_URL_IDX, baseTestUrl);
        parameters.add(USERNAME_IDX, username);
        parameters.add(PASSWORD_IDX, password);
        parameters.add(SPEC_NAME_IDX, specificationName);
        
        return parameters;
    }

   

}
