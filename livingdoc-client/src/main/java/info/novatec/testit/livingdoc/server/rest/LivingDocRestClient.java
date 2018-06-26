package info.novatec.testit.livingdoc.server.rest;

import info.novatec.testit.livingdoc.server.LivingDocServerErrorKey;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.domain.*;
import info.novatec.testit.livingdoc.server.rest.requests.*;
import info.novatec.testit.livingdoc.server.rest.responses.*;
import info.novatec.testit.livingdoc.util.ClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;


public class LivingDocRestClient implements RestClient {

    private static final Logger log = LoggerFactory.getLogger(LivingDocRestClient.class);

    private final String baseUrl;
    private final String username;
    private final String password;

    private final RestTemplate template;

    /**
     * Constructor
     *
     * @param baseUrl
     * @param username User to authenticate in Confluence. Null for anonymous access.
     * @param password
     */
    public LivingDocRestClient(final String baseUrl, final String username, final String password) {
        this(baseUrl, username, password, new RestTemplate());
    }

    /**
     * Constructor
     *
     * @param baseUrl
     * @param username User to authenticate in Confluence. Null for anonymous access.
     * @param password
     * @param template the {@link RestTemplate} to use
     */
    public LivingDocRestClient(String baseUrl, String username, String password, RestTemplate template) {
        this.baseUrl = baseUrl;
        this.username = username;
        this.password = password;
        this.template = template;
    }

    public RestTemplate getTemplate() {
        return template;
    }

    @Override
    public String getRenderedSpecification(List<?> args) throws LivingDocServerException {
        log.debug("Getting rendered specification...");
        GetRenderedSpecificationRequest request = new GetRenderedSpecificationRequest(args);
        GetRenderedSpecificationResponse response = exchangeRest(RestMethodName.getRenderedSpecification, request, GetRenderedSpecificationResponse.class);
        return response.getSpecification();
    }

    @Override
    public List<?> listDocumentsInHierarchy(List<?> args) throws LivingDocServerException {
        log.debug("Listing documents in hierarchy...");
        ListDocumentsInHierarchyRequest request = new ListDocumentsInHierarchyRequest(args);
        ListDocumentsInHierarchyResponse response = exchangeRest(RestMethodName.listDocumentsInHierarchy, request, ListDocumentsInHierarchyResponse.class);
        return response.getSpecifications();
    }

    @Override
    public String setSpecificationAsImplemented(List<?> args) throws LivingDocServerException {

        log.debug("Setting specification as implemented...");
        SetSpecificationAsImplementedRequest request = new SetSpecificationAsImplementedRequest(args);
        SetSpecificationAsImplementedResponse response = exchangeRest(RestMethodName.setSpecificationAsImplemented, request, SetSpecificationAsImplementedResponse.class);
        return response.getMessage();
    }

    @Override
    public boolean testConnection(final String hostName, String handler) throws LivingDocServerException {
        return testConnection();
    }

    @Override
    public boolean testConnection() throws LivingDocServerException {
        log.debug("Testing connection...");
        TestConnectionRequest request = new TestConnectionRequest();
        TestConnectionResponse response = exchangeRest(RestMethodName.testConnection, request, TestConnectionResponse.class);
        return response.success;
    }

    @Override
    public boolean ping(Repository repository, String identifier) throws LivingDocServerException {

        log.debug("Pinging : [identifier= % ]", identifier);
        PingRequest request = new PingRequest(repository);
        PingResponse response = exchangeRest(RestMethodName.ping, request, PingResponse.class);
        return response.success;
    }

    @Override
    public Runner getRunner(String name, String identifier) throws LivingDocServerException {

        log.debug("Retrieving all runners ...");
        GetRunnerRequest request = new GetRunnerRequest(name);
        GetRunnerResponse response = exchangeRest(RestMethodName.getRunner, request, GetRunnerResponse.class);
        return response.runner;
    }

    @Override
    public Set<Runner> getAllRunners(String identifier) throws LivingDocServerException {

        log.debug("Retrieving all runners ...");
        GetAllRunnersRequest request = new GetAllRunnersRequest();
        GetAllRunnersResponse response = exchangeRest(RestMethodName.getAllRunners, request, GetAllRunnersResponse.class);
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

        log.debug("Updating runner: %", oldRunnerName);
        UpdateRunnerRequest request = new UpdateRunnerRequest(oldRunnerName, runner);
        exchangeRest(RestMethodName.updateRunner, request, null);
    }

    @Override
    public void removeRunner(final String name, final String identifier) throws LivingDocServerException {

        log.debug("Removing runner: %", name);
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
    public void removeRepository(final String repositoryUid, final String identifier) throws LivingDocServerException {

        log.debug("Removing Repository %", repositoryUid);
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
        log.debug("Retrieving Specification repositories for Associated project. (SystemUnderTest : " + systemUnderTest.getName() + ")");
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

        log.debug("Retrieving SUT list for Project: %", projectName);
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
        HasRequirementReferencesRequest request = new HasRequirementReferencesRequest(requirement);
        HasRequirementReferencesResponse response =
                exchangeRest(RestMethodName.doesRequirementHasReferences, request, HasRequirementReferencesResponse.class);
        return response.value;
    }

    @Override
    public Set<Reference> getReferences(Requirement requirement, String identifier) throws LivingDocServerException {

        log.debug("Retrieving Requirement " + requirement.getName() + " References");
        GetRequirementReferencesRequest request = new GetRequirementReferencesRequest(requirement);
        GetRequirementReferencesResponse response =
                exchangeRest(RestMethodName.getRequirementReferences, request, GetRequirementReferencesResponse.class);
        return response.getReferences();
    }

    @Override
    public Reference getReference(Reference reference, String identifier) throws LivingDocServerException {

        log.debug("Retrieving Reference: " + reference.getRequirement().getName() + "," + reference.getSpecification().getName());
        GetReferenceRequest request = new GetReferenceRequest(reference);
        GetReferenceResponse response = exchangeRest(RestMethodName.getReference, request, GetReferenceResponse.class);
        return response.reference;
    }

    @Override
    public SystemUnderTest getSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository, String identifier)
            throws LivingDocServerException {

        log.debug("Retrieving SystemUnderTest: " + systemUnderTest.getName());
        GetSystemUnderTestRequest request = new GetSystemUnderTestRequest(systemUnderTest, repository);
        GetSystemUnderTestResponse response = exchangeRest(RestMethodName.getSystemUnderTest, request, GetSystemUnderTestResponse.class);
        return response.systemUnderTest;
    }

    @Override
    public void createSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository, String identifier)
            throws LivingDocServerException {

        log.debug("Creating SystemUnderTest: " + systemUnderTest.getName());
        CreateSystemUnderTestRequest request = new CreateSystemUnderTestRequest(systemUnderTest, repository);
        exchangeRest(RestMethodName.createSystemUnderTest, request, Void.class);
    }

    @Override
    public void updateSystemUnderTest(String oldSystemUnderTestName, SystemUnderTest newSystemUnderTest,
                                      Repository repository, String identifier) throws LivingDocServerException {

        log.debug("Updating SystemUnderTest: %", oldSystemUnderTestName);
        UpdateSystemUnderTestRequest request =
                new UpdateSystemUnderTestRequest(oldSystemUnderTestName, newSystemUnderTest, repository);
        exchangeRest(RestMethodName.updateSystemUnderTest, request, Void.class);
    }

    @Override
    public void removeSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository, String identifier)
            throws LivingDocServerException {

        log.debug("Removing SystemUnderTest: " + systemUnderTest.getName());
        RemoveSystemUnderTestRequest request = new RemoveSystemUnderTestRequest(systemUnderTest, repository);
        exchangeRest(RestMethodName.removeSystemUnderTest, request, Void.class);
    }

    @Override
    public void setSystemUnderTestAsDefault(SystemUnderTest systemUnderTest, Repository repository, String identifier)
            throws LivingDocServerException {

        log.debug("Setting as default the SystemUnderTest: " + systemUnderTest.getName());
        SetSystemUnderTestAsDefaultRequest request = new SetSystemUnderTestAsDefaultRequest(systemUnderTest, repository);
        exchangeRest(RestMethodName.setSystemUnderTestAsDefault, request, Void.class);
    }

    @Override
    public void removeRequirement(Requirement requirement, String identifier) throws LivingDocServerException {

        log.debug("Removing Requirement: " + requirement.getName());
        RemoveRequirementRequest request = new RemoveRequirementRequest(requirement);
        exchangeRest(RestMethodName.removeRequirement, request, Void.class);
    }

    @Override
    public Specification getSpecification(Specification specification, String identifier) throws LivingDocServerException {

        log.debug("Retrieving Specification: " + specification.getName());
        GetSpecificationRequest request = new GetSpecificationRequest(specification);
        GetSpecificationResponse response = exchangeRest(RestMethodName.getSpecification, request, GetSpecificationResponse.class);
        return response.specification;
    }

    @Override
    public Specification createSpecification(Specification specification, String identifier)
            throws LivingDocServerException {

        log.debug("Creating Specification: " + specification.getName());
        CreateSpecificationRequest request = new CreateSpecificationRequest(specification);
        CreateSpecificationResponse response = exchangeRest(RestMethodName.createSpecification, request, CreateSpecificationResponse.class);
        return response.specification;
    }

    @Override
    public void updateSpecification(Specification oldSpecification, Specification newSpecification, String identifier)
            throws LivingDocServerException {

        log.debug("Updating Specification: " + oldSpecification.getName());
        UpdateSpecificationRequest request = new UpdateSpecificationRequest(oldSpecification, newSpecification);
        exchangeRest(RestMethodName.updateSpecification, request, Void.class);
    }

    @Override
    public void removeSpecification(Specification specification, String identifier) throws LivingDocServerException {

        log.debug("Removing Specification: " + specification.getName());
        RemoveSpecificationRequest request = new RemoveSpecificationRequest(specification);
        exchangeRest(RestMethodName.removeSpecification, request, Void.class);
    }

    @Override
    public void createReference(Reference reference, String identifier) throws LivingDocServerException {

        log.debug("Creating Reference: " + reference.getRequirement().getName() + "," + reference.getSpecification().getName());
        CreateReferenceRequest request = new CreateReferenceRequest(reference);
        exchangeRest(RestMethodName.createReference, request, Void.class);
    }

    @Override
    public Reference updateReference(Reference oldReference, Reference newReference, String identifier)
            throws LivingDocServerException {

        log.debug("Updating Reference: " + newReference.getRequirement().getName() + "," + newReference.getSpecification()
                .getName());
        UpdateReferenceRequest request = new UpdateReferenceRequest(oldReference, newReference);
        UpdateReferenceResponse response = exchangeRest(RestMethodName.updateReference, request, UpdateReferenceResponse.class);
        return response.reference;
    }

    @Override
    public void removeReference(Reference reference, String identifier) throws LivingDocServerException {

        log.debug("Removing Reference: " + reference.getRequirement().getName() + "," + reference.getSpecification().getName());
        RemoveReferenceRequest request = new RemoveReferenceRequest(reference);
        exchangeRest(RestMethodName.removeReference, request, Void.class);
    }

    @Override
    public Execution runSpecification(SystemUnderTest systemUnderTest, Specification specification,
                                      boolean implementedVersion, String locale, String identifier) throws LivingDocServerException {

        log.debug("Running Specification: " + specification.getName() + " ON System:" + systemUnderTest.getName());
        RunSpecificationRequest request =
                new RunSpecificationRequest(systemUnderTest, specification, implementedVersion, locale);
        RunSpecificationResponse response = exchangeRest(RestMethodName.runSpecification, request, RunSpecificationResponse.class);
        return response.execution;
    }

    @Override
    public Reference runReference(Reference reference, String locale, String identifier) throws LivingDocServerException {

        log.debug("Running Reference: " + reference.getRequirement().getName() + "," + reference.getSpecification().getName());
        RunReferenceRequest request = new RunReferenceRequest(reference, locale);
        RunReferenceResponse response = exchangeRest(RestMethodName.runReference, request, RunReferenceResponse.class);
        return response.reference;
    }

    @Override
    public RequirementSummary getSummary(Requirement requirement, String identifier) throws LivingDocServerException {

        log.debug("Getting Requirement " + requirement.getName() + " summary");
        GetSummaryRequest request = new GetSummaryRequest(requirement);
        GetSummaryResponse response = exchangeRest(RestMethodName.getRequirementSummary, request, GetSummaryResponse.class);
        return response.requirementSummary;
    }

    @Override
    public DocumentNode getSpecificationHierarchy(Repository repository, SystemUnderTest systemUnderTest, String identifier)
            throws LivingDocServerException {

        log.debug("Get Specification Hierarchy: " + repository.getName() + " & " + systemUnderTest.getName());
        GetSpecificationHierarchyRequest request = new GetSpecificationHierarchyRequest(repository, systemUnderTest);
        GetSpecificationHierarchyResponse response =
                exchangeRest(RestMethodName.getSpecificationHierarchy, request, GetSpecificationHierarchyResponse.class);
        return response.documentNode;
    }

    @Override
    public List<List<String>> getListOfSpecificationLocations(String repoUID, String sut) throws LivingDocServerException {
        log.debug("Get List of Specification Location: repoUID : " + repoUID + "Sut : " + sut);
        GetListOfSpecificaitionLocationRequest request = new GetListOfSpecificaitionLocationRequest(repoUID, sut);
        GetListOfSpecificationLocationResponse response =
                exchangeRest(RestMethodName.getListOfSpecificationLocations, request, GetListOfSpecificationLocationResponse.class);
        return response.definitions;
    }

    @Override
    public String saveExecutionResult(List<?> args) throws LivingDocServerException {
        log.debug("Saving Execution Results");
        SaveExecutionResultRequest request = new SaveExecutionResultRequest(args);
        SaveExecutionResultResponse response =
                exchangeRest(RestMethodName.saveExecutionResult, request, SaveExecutionResultResponse.class);
        return response.message;
    }


    private <T> T exchangeRest(RestMethodName methodName, Object request, Class<T> responseType)
            throws LivingDocServerException {

        RequestEntity<Object> requestEntity;
        RequestEntity.BodyBuilder bodyBuilder = RequestEntity.post(getUri());
        bodyBuilder.contentType(MediaType.APPLICATION_JSON)
                .header("method-name", methodName.name());

        if (!isAnonymousAccess()) {
            try {
                bodyBuilder.header("Authorization", "Basic " + ClientUtils.encodeToBase64(":", username, password));
            } catch (UnsupportedEncodingException uee) {
                throw new LivingDocServerException(LivingDocServerErrorKey.CALL_FAILED, uee.getMessage(), uee);
            }
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
