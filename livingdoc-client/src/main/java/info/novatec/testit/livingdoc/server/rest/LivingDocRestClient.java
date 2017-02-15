package info.novatec.testit.livingdoc.server.rest;

import info.novatec.testit.livingdoc.server.LivingDocServerErrorKey;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.ServerPropertiesManager;
import info.novatec.testit.livingdoc.server.domain.*;
import info.novatec.testit.livingdoc.server.rest.requests.*;
import info.novatec.testit.livingdoc.server.rest.responses.*;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;


public class LivingDocRestClient implements RestClient {

    private static final Logger log = LoggerFactory.getLogger(LivingDocRestClient.class);

    private final String baseUrl;
    private final String username;
    private final String password;

    private RestTemplate template = new RestTemplate();

    /**
     * Constructor
     * @param baseUrl
     * @param username User to authenticate in Confluence. Null for anonymous access.
     * @param password
     */
    public LivingDocRestClient(final String baseUrl, final String username, final String password) {
        this.baseUrl = baseUrl;
        this.username = username;
        this.password = password;
    }

    /**
     * @return
     * @deprecated view {@link RestClient#getServerPropertiesManager}
     */
    @Deprecated
    @Override
    public ServerPropertiesManager getServerPropertiesManager() {
        throw new NotImplementedException("not implemented: getServerPropertiesManager");
    }

    @Override
    public boolean testConnection(final String hostName, String handler) throws LivingDocServerException {

        log.debug("Testing connection...");

        RestMethodName methodName = RestMethodName.testConnection;
        TestConnectionRequest request = new TestConnectionRequest();
        TestConnectionResponse response = exchangeRest(methodName, request, TestConnectionResponse.class);
        return response.success;
    }

    @Override
    public boolean ping(Repository repository, String identifier) throws LivingDocServerException {

        log.debug("Pinging : [identifier=" + identifier + "]");

        RestMethodName methodName = RestMethodName.ping;
        PingRequest request = new PingRequest(repository);
        PingResponse response = exchangeRest(methodName, request, PingResponse.class);
        return response.success;
    }

    @Override
    public Runner getRunner(String name, String identifier) throws LivingDocServerException {
        log.debug("Retrieving all runners ...");
        RestMethodName methodName = RestMethodName.getRunner;
        GetRunnerRequest request = new GetRunnerRequest(name);
        GetRunnerResponse response = exchangeRest(methodName, request, GetRunnerResponse.class);
        return response.runner;
    }

    @Override
    public Set<Runner> getAllRunners(String identifier) throws LivingDocServerException {
        log.debug("Retrieving all runners ...");
        RestMethodName methodName = RestMethodName.getAllRunners;
        GetAllRunnersRequest request = new GetAllRunnersRequest();
        GetAllRunnersResponse response = exchangeRest(methodName, request, GetAllRunnersResponse.class);
        return response.getRunners();
    }

    @Override
    public void createRunner(Runner runner, String identifier) throws LivingDocServerException {
        log.debug("Creating runner: " + runner.getName());

        CreateRunnerRequest request = new CreateRunnerRequest(runner);
        exchangeRest(RestMethodName.createRunner, request, null);
    }

    @Override
    public void updateRunner(final String oldRunnerName, Runner runner, String identifier) throws LivingDocServerException {
        log.debug("Updating runner: " + oldRunnerName);

        UpdateRunnerRequest request = new UpdateRunnerRequest(oldRunnerName, runner);
        exchangeRest(RestMethodName.updateRunner, request, null);
    }

    @Override
    public void removeRunner(String name, String identifier) throws LivingDocServerException {
        log.debug("Removing runner: " + name);

        RemoveRunnerRequest request = new RemoveRunnerRequest(name);
        exchangeRest(RestMethodName.removeRunner, request, null);
    }

    @Override
    public Repository getRegisteredRepository(Repository repository, String identifier) throws LivingDocServerException {
        log.debug("Retrieving Registered Repository: " + repository.getUid());
        GetRegisteredRepositoryRequest request = new GetRegisteredRepositoryRequest(repository);

        GetRegisteredRepositoryResponse response =
                exchangeRest(RestMethodName.getRegisteredRepository, request, GetRegisteredRepositoryResponse.class);
        return response.repository;
    }

    @Override
    public Repository registerRepository(Repository repository, String identifier) throws LivingDocServerException {
        log.debug("Registering Repository: " + repository.getUid());
        RegisterRepositoryRequest request = new RegisterRepositoryRequest(repository);

        RegisterRepositoryResponse response =
                exchangeRest(RestMethodName.registerRepository, request, RegisterRepositoryResponse.class);

        return response.repository;
    }

    @Override
    public void updateRepositoryRegistration(Repository repository, String identifier) throws LivingDocServerException {
        log.debug("Updating Repository registration: " + repository.getUid());

        UpdateRepositoryRegistrationRequest request = new UpdateRepositoryRegistrationRequest(repository);
        exchangeRest(RestMethodName.updateRepositoryRegistration, request, Void.class);
    }

    @Override
    public void removeRepository(String repositoryUid, String identifier) throws LivingDocServerException {
        log.debug("Removing Repository " + repositoryUid);

        RemoveRepositoryRequest request = new RemoveRepositoryRequest(repositoryUid);
        exchangeRest(RestMethodName.removeRepository, request, Void.class);
    }

    @Override
    public Set<Project> getAllProjects(String identifier) throws LivingDocServerException {
        log.debug("Retrieving All Projects");

        GetAllProjectsRequest request = new GetAllProjectsRequest();
        GetAllProjectsResponse response =
                exchangeRest(RestMethodName.getAllProjects, request, GetAllProjectsResponse.class);

        return response.getAllProjects();
    }

    @Override
    public Set<Repository> getAllSpecificationRepositories(String identifier) throws LivingDocServerException {
        log.debug("Retrieving all specification repositories.");

        GetAllSpecificationRepositoriesRequest request = new GetAllSpecificationRepositoriesRequest();

        GetAllSpecificationRepositoriesResponse response =
                exchangeRest(RestMethodName.getAllSpecificationRepositories, request,
                        GetAllSpecificationRepositoriesResponse.class);

        return response.getAllSpecificationRepositories();
    }

    @Override
    public Set<Repository> getAllRepositoriesForSystemUnderTest(SystemUnderTest systemUnderTest, String identifier)
            throws LivingDocServerException {
        log.debug("Retrieving repositories for Associated project. (SystemUnderTest : " + systemUnderTest.getName() + ")");

        GetAllRepositoriesForSystemUnderTestRequest request = new GetAllRepositoriesForSystemUnderTestRequest(systemUnderTest);

        GetAllRepositoriesForSystemUnderTestResponse response =
                exchangeRest(RestMethodName.getAllRepositoriesForSystemUnderTest, request,
                        GetAllRepositoriesForSystemUnderTestResponse.class);

        return response.getAllRepositoriesForSystemUnderTest();
    }

    @Override
    public Set<Repository> getSpecificationRepositoriesOfAssociatedProject(Repository repository, String identifier)
            throws LivingDocServerException {
        log.debug("Retrieving Specification repositories for Associated project. (Repo UID: " + repository.getUid() + ")");

        GetSpecificationRepositoriesOfAssociatedProjectRequest request =
                new GetSpecificationRepositoriesOfAssociatedProjectRequest(repository);

        GetSpecificationRepositoriesOfAssociatedProjectResponse response =
                exchangeRest(RestMethodName.getSpecificationRepositoriesOfAssociatedProject, request,
                        GetSpecificationRepositoriesOfAssociatedProjectResponse.class);

        return response.getSpecificationRepositoriesOfAssociatedProject();
    }

    @Override
    public Set<Repository> getSpecificationRepositoriesOfAssociatedProject(SystemUnderTest systemUnderTest,
                                                                           String identifier) throws LivingDocServerException {
        log.debug(
                "Retrieving Specification repositories for Associated project. (SystemUnderTest : " + systemUnderTest.getName()
                        + ")");
        GetSpecificationRepositoriesForSystemUnderTestRequest request =
                new GetSpecificationRepositoriesForSystemUnderTestRequest(systemUnderTest);

        GetSpecificationRepositoriesForSystemUnderTestResponse response =
                exchangeRest(RestMethodName.getSpecificationRepositoriesForSystemUnderTest, request,
                        GetSpecificationRepositoriesForSystemUnderTestResponse.class);

        return response.getSpecificationRepositoriesOfAssociatedProject();
    }

    @Override
    public Set<Repository> getRequirementRepositoriesOfAssociatedProject(Repository repository, String identifier)
            throws LivingDocServerException {
        log.debug("Retrieving Requirement repositories for Associated project. (Repo UID: " + repository.getUid() + ")");

        GetRequirementRepositoriesOfAssociatedProjectRequest request =
                new GetRequirementRepositoriesOfAssociatedProjectRequest(repository);

        GetRequirementRepositoriesOfAssociatedProjectResponse response =
                exchangeRest(RestMethodName.getRequirementRepositoriesOfAssociatedProject, request,
                        GetRequirementRepositoriesOfAssociatedProjectResponse.class);

        return response.getRequirementRepositoriesOfAssociatedProject();
    }

    @Override
    public Set<SystemUnderTest> getSystemUnderTestsOfAssociatedProject(Repository repository, String identifier)
            throws LivingDocServerException {
        log.debug("Retrieving SUT list for Associated repository: " + repository.getName());

        GetSystemUnderTestsOfAssociatedProjectRequest request =
                new GetSystemUnderTestsOfAssociatedProjectRequest(repository);

        GetSystemUnderTestsOfAssociatedProjectResponse response =
                exchangeRest(RestMethodName.getSystemUnderTestsOfAssociatedProject, request,
                        GetSystemUnderTestsOfAssociatedProjectResponse.class);

        return response.getSystemUnderTestsOfAssociatedProject();
    }

    @Override
    public Set<SystemUnderTest> getSystemUnderTestsOfProject(String projectName, String identifier)
            throws LivingDocServerException {
        log.debug("Retrieving SUT list for Project: " + projectName);

        GetSystemUnderTestsOfProjectRequest request = new GetSystemUnderTestsOfProjectRequest(projectName);

        GetSystemUnderTestsOfProjectResponse response =
                exchangeRest(RestMethodName.getSystemUnderTestsOfProject, request, GetSystemUnderTestsOfProjectResponse.class);

        return response.getSystemUnderTestsOfProject();
    }

    @Override
    public void addSystemUnderTest(SystemUnderTest systemUnderTest, Specification specification, String identifier)
            throws LivingDocServerException {
        log.debug("Adding SUT " + systemUnderTest.getName() + " to SUT list of specification: " + specification.getName());

        AddSpecificationSystemUnderTestRequest request =
                new AddSpecificationSystemUnderTestRequest(systemUnderTest, specification);

        exchangeRest(RestMethodName.addSpecificationSystemUnderTest, request, Void.class);
    }

    @Override
    public void removeSystemUnderTest(SystemUnderTest systemUnderTest, Specification specification, String identifier)
            throws LivingDocServerException {
        log.debug("Adding SUT " + systemUnderTest.getName() + " to SUT list of specification: " + specification.getName());

        RemoveSpecificationSystemUnderTestRequest request =
                new RemoveSpecificationSystemUnderTestRequest(systemUnderTest, specification);

        exchangeRest(RestMethodName.removeSpecificationSystemUnderTest, request, Void.class);
    }

    @Override
    public boolean hasReferences(Specification specification, String identifier) throws LivingDocServerException {
        log.debug("Does specification " + specification.getName() + " Has References");

        DoesSpecificationHasReferencesRequest request = new DoesSpecificationHasReferencesRequest(specification);

        DoesSpecificationHasReferencesResponse response =
                exchangeRest(RestMethodName.doesSpecificationHasReferences, request,
                        DoesSpecificationHasReferencesResponse.class);

        return response.hasReferences();
    }

    @Override
    public Set<Reference> getReferences(Specification specification, String identifier) throws LivingDocServerException {
        log.debug("Retrieving Specification " + specification.getName() + " References");

        GetSpecificationReferencesRequest request = new GetSpecificationReferencesRequest(specification);

        GetSpecificationReferencesResponse response =
                exchangeRest(RestMethodName.getSpecificationReferences, request, GetSpecificationReferencesResponse.class);

        return response.getReferences();
    }

    @Override
    public boolean hasReferences(Requirement requirement, String identifier) throws LivingDocServerException {
        log.debug("Does Requirement " + requirement.getName() + " Has References");
        RestMethodName methodName = RestMethodName.doesRequirementHasReferences;
        HasRequirementReferencesRequest request = new HasRequirementReferencesRequest(requirement);
        HasRequirementReferencesResponse response =
                exchangeRest(methodName, request, HasRequirementReferencesResponse.class);
        return response.value;
    }

    @Override
    public Set<Reference> getReferences(Requirement requirement, String identifier) throws LivingDocServerException {
        log.debug("Retrieving Requirement " + requirement.getName() + " References");
        RestMethodName methodName = RestMethodName.getRequirementReferences;
        GetRequirementReferencesRequest request = new GetRequirementReferencesRequest(requirement);
        GetRequirementReferencesResponse response =
                exchangeRest(methodName, request, GetRequirementReferencesResponse.class);
        return response.getReferences();
    }

    @Override
    public Reference getReference(Reference reference, String identifier) throws LivingDocServerException {
        log.debug(
                "Retrieving Reference: " + reference.getRequirement().getName() + "," + reference.getSpecification().getName());
        RestMethodName methodName = RestMethodName.getReference;
        GetReferenceRequest request = new GetReferenceRequest(reference);
        GetReferenceResponse response = exchangeRest(methodName, request, GetReferenceResponse.class);
        return response.reference;
    }

    @Override
    public SystemUnderTest getSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository, String identifier)
            throws LivingDocServerException {
        log.debug("Retrieving SystemUnderTest: " + systemUnderTest.getName());
        RestMethodName methodName = RestMethodName.getSystemUnderTest;
        GetSystemUnderTestRequest request = new GetSystemUnderTestRequest(systemUnderTest, repository);
        GetSystemUnderTestResponse response = exchangeRest(methodName, request, GetSystemUnderTestResponse.class);
        return response.systemUnderTest;
    }

    @Override
    public void createSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository, String identifier)
            throws LivingDocServerException {
        log.debug("Creating SystemUnderTest: " + systemUnderTest.getName());
        RestMethodName methodName = RestMethodName.createSystemUnderTest;
        CreateSystemUnderTestRequest request = new CreateSystemUnderTestRequest(systemUnderTest, repository);
        exchangeRest(methodName, request, Void.class);
    }

    @Override
    public void updateSystemUnderTest(String oldSystemUnderTestName, SystemUnderTest newSystemUnderTest,
                                      Repository repository, String identifier) throws LivingDocServerException {
        log.debug("Updating SystemUnderTest: " + oldSystemUnderTestName);
        RestMethodName methodName = RestMethodName.updateSystemUnderTest;
        UpdateSystemUnderTestRequest request =
                new UpdateSystemUnderTestRequest(oldSystemUnderTestName, newSystemUnderTest, repository);
        exchangeRest(methodName, request, Void.class);
    }

    @Override
    public void removeSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository, String identifier)
            throws LivingDocServerException {
        log.debug("Removing SystemUnderTest: " + systemUnderTest.getName());
        RestMethodName methodName = RestMethodName.removeSystemUnderTest;
        RemoveSystemUnderTestRequest request = new RemoveSystemUnderTestRequest(systemUnderTest, repository);
        exchangeRest(methodName, request, Void.class);
    }

    @Override
    public void setSystemUnderTestAsDefault(SystemUnderTest systemUnderTest, Repository repository, String identifier)
            throws LivingDocServerException {
        log.debug("Setting as default the SystemUnderTest: " + systemUnderTest.getName());
        RestMethodName methodName = RestMethodName.setSystemUnderTestAsDefault;
        SetSystemUnderTestAsDefaultRequest request = new SetSystemUnderTestAsDefaultRequest(systemUnderTest, repository);
        exchangeRest(methodName, request, Void.class);
    }

    @Override
    public void removeRequirement(Requirement requirement, String identifier) throws LivingDocServerException {
        log.debug("Removing Requirement: " + requirement.getName());
        RestMethodName methodName = RestMethodName.removeRequirement;
        RemoveRequirementRequest request = new RemoveRequirementRequest(requirement);
        exchangeRest(methodName, request, Void.class);
    }

    @Override
    public Specification getSpecification(Specification specification, String identifier) throws LivingDocServerException {
        log.debug("Retrieving Specification: " + specification.getName());
        RestMethodName methodName = RestMethodName.getSpecification;
        GetSpecificationRequest request = new GetSpecificationRequest(specification);
        GetSpecificationResponse response = exchangeRest(methodName, request, GetSpecificationResponse.class);
        return response.specification;
    }

    @Override
    public Specification createSpecification(Specification specification, String identifier)
            throws LivingDocServerException {
        log.debug("Creating Specification: " + specification.getName());
        RestMethodName methodName = RestMethodName.createSpecification;
        CreateSpecificationRequest request = new CreateSpecificationRequest(specification);
        CreateSpecificationResponse response = exchangeRest(methodName, request, CreateSpecificationResponse.class);
        return response.specification;
    }

    @Override
    public void updateSpecification(Specification oldSpecification, Specification newSpecification, String identifier)
            throws LivingDocServerException {
        log.debug("Updating Specification: " + oldSpecification.getName());
        RestMethodName methodName = RestMethodName.updateSpecification;
        UpdateSpecificationRequest request = new UpdateSpecificationRequest(oldSpecification, newSpecification);
        exchangeRest(methodName, request, Void.class);
    }

    @Override
    public void removeSpecification(Specification specification, String identifier) throws LivingDocServerException {
        log.debug("Removing Specification: " + specification.getName());
        RestMethodName methodName = RestMethodName.removeSpecification;
        RemoveSpecificationRequest request = new RemoveSpecificationRequest(specification);
        exchangeRest(methodName, request, Void.class);
    }

    @Override
    public void createReference(Reference reference, String identifier) throws LivingDocServerException {
        log.debug(
                "Creating Reference: " + reference.getRequirement().getName() + "," + reference.getSpecification().getName());
        RestMethodName methodName = RestMethodName.createReference;
        CreateReferenceRequest request = new CreateReferenceRequest(reference);
        exchangeRest(methodName, request, Void.class);
    }

    @Override
    public Reference updateReference(Reference oldReference, Reference newReference, String identifier)
            throws LivingDocServerException {
        log.debug("Updating Reference: " + newReference.getRequirement().getName() + "," + newReference.getSpecification()
                .getName());
        RestMethodName methodName = RestMethodName.updateReference;
        UpdateReferenceRequest request = new UpdateReferenceRequest(oldReference, newReference);
        UpdateReferenceResponse response = exchangeRest(methodName, request, UpdateReferenceResponse.class);
        return response.reference;
    }

    @Override
    public void removeReference(Reference reference, String identifier) throws LivingDocServerException {
        log.debug(
                "Removing Reference: " + reference.getRequirement().getName() + "," + reference.getSpecification().getName());
        RestMethodName methodName = RestMethodName.removeReference;
        RemoveReferenceRequest request = new RemoveReferenceRequest(reference);
        exchangeRest(methodName, request, Void.class);
    }

    @Override
    public Execution runSpecification(SystemUnderTest systemUnderTest, Specification specification,
                                      boolean implementedVersion, String locale, String identifier) throws LivingDocServerException {
        log.debug("Running Specification: " + specification.getName() + " ON System:" + systemUnderTest.getName());
        RestMethodName methodName = RestMethodName.runSpecification;
        RunSpecificationRequest request =
                new RunSpecificationRequest(systemUnderTest, specification, implementedVersion, locale);
        RunSpecificationResponse response = exchangeRest(methodName, request, RunSpecificationResponse.class);
        return response.execution;
    }

    @Override
    public Reference runReference(Reference reference, String locale, String identifier) throws LivingDocServerException {
        log.debug(
                "Running Reference: " + reference.getRequirement().getName() + "," + reference.getSpecification().getName());
        RestMethodName methodName = RestMethodName.runReference;
        RunReferenceRequest request = new RunReferenceRequest(reference, locale);
        RunReferenceResponse response = exchangeRest(methodName, request, RunReferenceResponse.class);
        return response.reference;
    }

    @Override
    public RequirementSummary getSummary(Requirement requirement, String identifier) throws LivingDocServerException {
        log.debug("Getting Requirement " + requirement.getName() + " summary");
        RestMethodName methodName = RestMethodName.getRequirementSummary;
        GetSummaryRequest request = new GetSummaryRequest(requirement);
        GetSummaryResponse response = exchangeRest(methodName, request, GetSummaryResponse.class);
        return response.requirementSummary;
    }

    @Override
    public DocumentNode getSpecificationHierarchy(Repository repository, SystemUnderTest systemUnderTest, String identifier)
            throws LivingDocServerException {
        log.debug("Get Specification Hierarchy: " + repository.getName() + " & " + systemUnderTest.getName());
        RestMethodName methodName = RestMethodName.getSpecificationHierarchy;
        GetSpecificationHierarchyRequest request = new GetSpecificationHierarchyRequest(repository, systemUnderTest);
        GetSpecificationHierarchyResponse response =
                exchangeRest(methodName, request, GetSpecificationHierarchyResponse.class);
        return response.documentNode;
    }

    private <T> T exchangeRest(RestMethodName methodName, Object request, Class<T> responseType)
            throws LivingDocServerException {

        RequestEntity<Object> requestEntity;
        RequestEntity.BodyBuilder bodyBuilder = RequestEntity.post(getUri());
        bodyBuilder.contentType(MediaType.APPLICATION_JSON)
                .header("method-name", methodName.name());

        if(!isAnonymousAccess()) {
            bodyBuilder.header("Authorization", "Basic " + getCredentialsInBase64());
        }
        requestEntity = bodyBuilder.body(request);

        ResponseEntity<T> responseEntity = template.exchange(requestEntity, responseType);

        HttpStatus statusCode = responseEntity.getStatusCode();
        if (!HttpStatus.OK.equals(statusCode)) {
            throw new LivingDocServerException(LivingDocServerErrorKey.CALL_FAILED,
                    "call was not successful, status: " + statusCode);
        }
        return responseEntity.getBody();
    }

    private boolean isAnonymousAccess() {
        return StringUtils.isBlank(username);
    }

    private String getCredentialsInBase64() {
       return Base64Utils.encodeToString((username+":"+password).getBytes());
    }

    private URI getUri() throws LivingDocServerException {

        try {
            String url = baseUrl;

            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            url = url + "/rest/livingdoc/1.0/command";
            return new URI(url);

        } catch (URISyntaxException e) {
            throw new LivingDocServerException(e);
        }
    }
}
