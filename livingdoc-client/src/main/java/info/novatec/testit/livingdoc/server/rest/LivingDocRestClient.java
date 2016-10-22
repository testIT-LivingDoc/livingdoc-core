package info.novatec.testit.livingdoc.server.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import info.novatec.testit.livingdoc.server.LivingDocServerErrorKey;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.ServerPropertiesManager;
import info.novatec.testit.livingdoc.server.domain.DocumentNode;
import info.novatec.testit.livingdoc.server.domain.Execution;
import info.novatec.testit.livingdoc.server.domain.Project;
import info.novatec.testit.livingdoc.server.domain.Reference;
import info.novatec.testit.livingdoc.server.domain.Repository;
import info.novatec.testit.livingdoc.server.domain.Requirement;
import info.novatec.testit.livingdoc.server.domain.RequirementSummary;
import info.novatec.testit.livingdoc.server.domain.Runner;
import info.novatec.testit.livingdoc.server.domain.Specification;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;
import info.novatec.testit.livingdoc.server.rest.requests.AddSpecificationSystemUnderTestRequest;
import info.novatec.testit.livingdoc.server.rest.requests.CreateReferenceRequest;
import info.novatec.testit.livingdoc.server.rest.requests.CreateRunnerRequest;
import info.novatec.testit.livingdoc.server.rest.requests.CreateSpecificationRequest;
import info.novatec.testit.livingdoc.server.rest.requests.CreateSystemUnderTestRequest;
import info.novatec.testit.livingdoc.server.rest.requests.DoesSpecificationHasReferencesRequest;
import info.novatec.testit.livingdoc.server.rest.requests.GetAllProjectsRequest;
import info.novatec.testit.livingdoc.server.rest.requests.GetAllRepositoriesForSystemUnderTestRequest;
import info.novatec.testit.livingdoc.server.rest.requests.GetAllRunnersRequest;
import info.novatec.testit.livingdoc.server.rest.requests.GetAllSpecificationRepositoriesRequest;
import info.novatec.testit.livingdoc.server.rest.requests.GetReferenceRequest;
import info.novatec.testit.livingdoc.server.rest.requests.GetRegisteredRepositoryRequest;
import info.novatec.testit.livingdoc.server.rest.requests.GetRequirementReferencesRequest;
import info.novatec.testit.livingdoc.server.rest.requests.GetRequirementRepositoriesOfAssociatedProjectRequest;
import info.novatec.testit.livingdoc.server.rest.requests.GetRunnerRequest;
import info.novatec.testit.livingdoc.server.rest.requests.GetSpecificationHierarchyRequest;
import info.novatec.testit.livingdoc.server.rest.requests.GetSpecificationReferencesRequest;
import info.novatec.testit.livingdoc.server.rest.requests.GetSpecificationRepositoriesForSystemUnderTestRequest;
import info.novatec.testit.livingdoc.server.rest.requests.GetSpecificationRepositoriesOfAssociatedProjectRequest;
import info.novatec.testit.livingdoc.server.rest.requests.GetSpecificationRequest;
import info.novatec.testit.livingdoc.server.rest.requests.GetSummaryRequest;
import info.novatec.testit.livingdoc.server.rest.requests.GetSystemUnderTestRequest;
import info.novatec.testit.livingdoc.server.rest.requests.GetSystemUnderTestsOfAssociatedProjectRequest;
import info.novatec.testit.livingdoc.server.rest.requests.GetSystemUnderTestsOfProjectRequest;
import info.novatec.testit.livingdoc.server.rest.requests.HasRequirementReferencesRequest;
import info.novatec.testit.livingdoc.server.rest.requests.RegisterRepositoryRequest;
import info.novatec.testit.livingdoc.server.rest.requests.RemoveReferenceRequest;
import info.novatec.testit.livingdoc.server.rest.requests.RemoveRepositoryRequest;
import info.novatec.testit.livingdoc.server.rest.requests.RemoveRequirementRequest;
import info.novatec.testit.livingdoc.server.rest.requests.RemoveRunnerRequest;
import info.novatec.testit.livingdoc.server.rest.requests.RemoveSpecificationRequest;
import info.novatec.testit.livingdoc.server.rest.requests.RemoveSpecificationSystemUnderTestRequest;
import info.novatec.testit.livingdoc.server.rest.requests.RemoveSystemUnderTestRequest;
import info.novatec.testit.livingdoc.server.rest.requests.RunReferenceRequest;
import info.novatec.testit.livingdoc.server.rest.requests.RunSpecificationRequest;
import info.novatec.testit.livingdoc.server.rest.requests.SetSystemUnderTestAsDefaultRequest;
import info.novatec.testit.livingdoc.server.rest.requests.UpdateReferenceRequest;
import info.novatec.testit.livingdoc.server.rest.requests.UpdateRepositoryRegistrationRequest;
import info.novatec.testit.livingdoc.server.rest.requests.UpdateRunnerRequest;
import info.novatec.testit.livingdoc.server.rest.requests.UpdateSpecificationRequest;
import info.novatec.testit.livingdoc.server.rest.requests.UpdateSystemUnderTestRequest;
import info.novatec.testit.livingdoc.server.rest.responses.CreateSpecificationResponse;
import info.novatec.testit.livingdoc.server.rest.responses.DoesSpecificationHasReferencesResponse;
import info.novatec.testit.livingdoc.server.rest.responses.GetAllProjectsResponse;
import info.novatec.testit.livingdoc.server.rest.responses.GetAllRepositoriesForSystemUnderTestResponse;
import info.novatec.testit.livingdoc.server.rest.responses.GetAllRunnersResponse;
import info.novatec.testit.livingdoc.server.rest.responses.GetAllSpecificationRepositoriesResponse;
import info.novatec.testit.livingdoc.server.rest.responses.GetReferenceResponse;
import info.novatec.testit.livingdoc.server.rest.responses.GetRegisteredRepositoryResponse;
import info.novatec.testit.livingdoc.server.rest.responses.GetRequirementReferencesResponse;
import info.novatec.testit.livingdoc.server.rest.responses.GetRequirementRepositoriesOfAssociatedProjectResponse;
import info.novatec.testit.livingdoc.server.rest.responses.GetRunnerResponse;
import info.novatec.testit.livingdoc.server.rest.responses.GetSpecificationHierarchyResponse;
import info.novatec.testit.livingdoc.server.rest.responses.GetSpecificationReferencesResponse;
import info.novatec.testit.livingdoc.server.rest.responses.GetSpecificationRepositoriesForSystemUnderTestResponse;
import info.novatec.testit.livingdoc.server.rest.responses.GetSpecificationRepositoriesOfAssociatedProjectResponse;
import info.novatec.testit.livingdoc.server.rest.responses.GetSpecificationResponse;
import info.novatec.testit.livingdoc.server.rest.responses.GetSummaryResponse;
import info.novatec.testit.livingdoc.server.rest.responses.GetSystemUnderTestResponse;
import info.novatec.testit.livingdoc.server.rest.responses.GetSystemUnderTestsOfAssociatedProjectResponse;
import info.novatec.testit.livingdoc.server.rest.responses.GetSystemUnderTestsOfProjectResponse;
import info.novatec.testit.livingdoc.server.rest.responses.HasRequirementReferencesResponse;
import info.novatec.testit.livingdoc.server.rest.responses.RegisterRepositoryResponse;
import info.novatec.testit.livingdoc.server.rest.responses.RunReferenceResponse;
import info.novatec.testit.livingdoc.server.rest.responses.RunSpecificationResponse;
import info.novatec.testit.livingdoc.server.rest.responses.UpdateReferenceResponse;
import info.novatec.testit.livingdoc.server.rpc.RpcClientService;
import info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcMethodName;


public class LivingDocRestClient implements RpcClientService {

    private static final Logger log = LoggerFactory.getLogger(LivingDocRestClient.class);

    private final String baseUrl;
    private RestTemplate template = new RestTemplate();

    public LivingDocRestClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public boolean testConnection(String hostName, String handler) throws LivingDocServerException {
        // TODO
        throw new NotImplementedException("not implemented: testConnection");
    }

    @Override
    public boolean ping(Repository repository, String identifier) throws LivingDocServerException {
        // TODO
        throw new NotImplementedException("not implemented: ping");
    }

    @Override
    public Runner getRunner(String name, String identifier) throws LivingDocServerException {
        log.debug("Retrieving all runners ...");
        XmlRpcMethodName methodName = XmlRpcMethodName.getRunner;
        GetRunnerRequest request = new GetRunnerRequest(name);
        GetRunnerResponse response = exchangeRest(methodName, request, GetRunnerResponse.class);
        return response.runner;
    }

    @Override
    public Set<Runner> getAllRunners(String identifier) throws LivingDocServerException {
        log.debug("Retrieving all runners ...");
        XmlRpcMethodName methodName = XmlRpcMethodName.getAllRunners;
        GetAllRunnersRequest request = new GetAllRunnersRequest();
        GetAllRunnersResponse response = exchangeRest(methodName, request, GetAllRunnersResponse.class);
        return response.getRunners();
    }

    @Override
    public void createRunner(Runner runner, String identifier) throws LivingDocServerException {
        log.debug("Creating runner: " + runner.getName());

        CreateRunnerRequest request = new CreateRunnerRequest(runner);
        exchangeRest(XmlRpcMethodName.createRunner, request, null);
    }

    @Override
    public void updateRunner(String oldRunnerName, Runner runner, String identifier) throws LivingDocServerException {
        log.debug("Updating runner: " + oldRunnerName);

        UpdateRunnerRequest request = new UpdateRunnerRequest(runner);
        exchangeRest(XmlRpcMethodName.updateRunner, request, null);
    }

    @Override
    public void removeRunner(String name, String identifier) throws LivingDocServerException {
        log.debug("Removing runner: " + name);

        RemoveRunnerRequest request = new RemoveRunnerRequest(name);
        exchangeRest(XmlRpcMethodName.removeRunner, request, null);
    }

    @Override
    public Repository getRegisteredRepository(Repository repository, String identifier) throws LivingDocServerException {
        log.debug("Retrieving Registered Repository: " + repository.getUid());
        GetRegisteredRepositoryRequest request = new GetRegisteredRepositoryRequest(repository);

        GetRegisteredRepositoryResponse response =
            exchangeRest(XmlRpcMethodName.getRegisteredRepository, request, GetRegisteredRepositoryResponse.class);
        return response.repository;
    }

    @Override
    public Repository registerRepository(Repository repository, String identifier) throws LivingDocServerException {
        log.debug("Registering Repository: " + repository.getUid());
        RegisterRepositoryRequest request = new RegisterRepositoryRequest(repository);

        RegisterRepositoryResponse response =
            exchangeRest(XmlRpcMethodName.registerRepository, request, RegisterRepositoryResponse.class);

        return response.repository;
    }

    @Override
    public void updateRepositoryRegistration(Repository repository, String identifier) throws LivingDocServerException {
        log.debug("Updating Repository registration: " + repository.getUid());

        UpdateRepositoryRegistrationRequest request = new UpdateRepositoryRegistrationRequest(repository);
        exchangeRest(XmlRpcMethodName.updateRepositoryRegistration, request, Void.class);
    }

    @Override
    public void removeRepository(String repositoryUid, String identifier) throws LivingDocServerException {
        log.debug("Removing Repository " + repositoryUid);

        RemoveRepositoryRequest request = new RemoveRepositoryRequest(repositoryUid);
        exchangeRest(XmlRpcMethodName.removeRepository, request, Void.class);
    }

    @Override
    public Set<Project> getAllProjects(String identifier) throws LivingDocServerException {
        log.debug("Retrieving All Projects");

        GetAllProjectsRequest request = new GetAllProjectsRequest();
        GetAllProjectsResponse response =
            exchangeRest(XmlRpcMethodName.getAllProjects, request, GetAllProjectsResponse.class);

        return response.getAllProjects();
    }

    @Override
    public Set<Repository> getAllSpecificationRepositories(String identifier) throws LivingDocServerException {
        log.debug("Retrieving all specification repositories.");

        GetAllSpecificationRepositoriesRequest request = new GetAllSpecificationRepositoriesRequest();

        GetAllSpecificationRepositoriesResponse response =
            exchangeRest(XmlRpcMethodName.getAllSpecificationRepositories, request,
                GetAllSpecificationRepositoriesResponse.class);

        return response.getAllSpecificationRepositories();
    }

    @Override
    public Set<Repository> getAllRepositoriesForSystemUnderTest(SystemUnderTest systemUnderTest, String identifier)
        throws LivingDocServerException {
        log.debug("Retrieving repositories for Associated project. (SystemUnderTest : " + systemUnderTest.getName() + ")");

        GetAllRepositoriesForSystemUnderTestRequest request = new GetAllRepositoriesForSystemUnderTestRequest();

        GetAllRepositoriesForSystemUnderTestResponse response =
            exchangeRest(XmlRpcMethodName.getAllRepositoriesForSystemUnderTest, request,
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
            exchangeRest(XmlRpcMethodName.getSpecificationRepositoriesOfAssociatedProject, request,
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
            exchangeRest(XmlRpcMethodName.getSpecificationRepositoriesForSystemUnderTest, request,
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
            exchangeRest(XmlRpcMethodName.getRequirementRepositoriesOfAssociatedProject, request,
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
            exchangeRest(XmlRpcMethodName.getSystemUnderTestsOfAssociatedProject, request,
                GetSystemUnderTestsOfAssociatedProjectResponse.class);

        return response.getSystemUnderTestsOfAssociatedProject();
    }

    @Override
    public Set<SystemUnderTest> getSystemUnderTestsOfProject(String projectName, String identifier)
        throws LivingDocServerException {
        log.debug("Retrieving SUT list for Project: " + projectName);

        GetSystemUnderTestsOfProjectRequest request = new GetSystemUnderTestsOfProjectRequest(projectName);

        GetSystemUnderTestsOfProjectResponse response =
            exchangeRest(XmlRpcMethodName.getSystemUnderTestsOfProject, request, GetSystemUnderTestsOfProjectResponse.class);

        return response.getSystemUnderTestsOfProject();
    }

    @Override
    public void addSystemUnderTest(SystemUnderTest systemUnderTest, Specification specification, String identifier)
        throws LivingDocServerException {
        log.debug("Adding SUT " + systemUnderTest.getName() + " to SUT list of specification: " + specification.getName());

        AddSpecificationSystemUnderTestRequest request =
            new AddSpecificationSystemUnderTestRequest(systemUnderTest, specification);

        exchangeRest(XmlRpcMethodName.addSpecificationSystemUnderTest, request, Void.class);
    }

    @Override
    public void removeSystemUnderTest(SystemUnderTest systemUnderTest, Specification specification, String identifier)
        throws LivingDocServerException {
        log.debug("Adding SUT " + systemUnderTest.getName() + " to SUT list of specification: " + specification.getName());

        RemoveSpecificationSystemUnderTestRequest request =
            new RemoveSpecificationSystemUnderTestRequest(systemUnderTest, specification);

        exchangeRest(XmlRpcMethodName.removeSpecificationSystemUnderTest, request, Void.class);
    }

    @Override
    public boolean hasReferences(Specification specification, String identifier) throws LivingDocServerException {
        log.debug("Does specification " + specification.getName() + " Has References");

        DoesSpecificationHasReferencesRequest request = new DoesSpecificationHasReferencesRequest(specification);

        DoesSpecificationHasReferencesResponse response =
            exchangeRest(XmlRpcMethodName.doesSpecificationHasReferences, request,
                DoesSpecificationHasReferencesResponse.class);

        return response.hasReferences();
    }

    @Override
    public Set<Reference> getReferences(Specification specification, String identifier) throws LivingDocServerException {
        log.debug("Retrieving Specification " + specification.getName() + " References");

        GetSpecificationReferencesRequest request = new GetSpecificationReferencesRequest(specification);

        GetSpecificationReferencesResponse response =
            exchangeRest(XmlRpcMethodName.getSpecificationReferences, request, GetSpecificationReferencesResponse.class);

        return response.getReferences();
    }

    @Override
    public boolean hasReferences(Requirement requirement, String identifier) throws LivingDocServerException {
        log.debug("Does Requirement " + requirement.getName() + " Has References");
        XmlRpcMethodName methodName = XmlRpcMethodName.doesRequirementHasReferences;
        HasRequirementReferencesRequest request = new HasRequirementReferencesRequest(requirement);
        HasRequirementReferencesResponse response =
            exchangeRest(methodName, request, HasRequirementReferencesResponse.class);
        return response.value;
    }

    @Override
    public Set<Reference> getReferences(Requirement requirement, String identifier) throws LivingDocServerException {
        log.debug("Retrieving Requirement " + requirement.getName() + " References");
        XmlRpcMethodName methodName = XmlRpcMethodName.getRequirementReferences;
        GetRequirementReferencesRequest request = new GetRequirementReferencesRequest(requirement);
        GetRequirementReferencesResponse response =
            exchangeRest(methodName, request, GetRequirementReferencesResponse.class);
        return response.getReferences();
    }

    @Override
    public Reference getReference(Reference reference, String identifier) throws LivingDocServerException {
        log.debug(
            "Retrieving Reference: " + reference.getRequirement().getName() + "," + reference.getSpecification().getName());
        XmlRpcMethodName methodName = XmlRpcMethodName.getReference;
        GetReferenceRequest request = new GetReferenceRequest(reference);
        GetReferenceResponse response = exchangeRest(methodName, request, GetReferenceResponse.class);
        return response.reference;
    }

    @Override
    public SystemUnderTest getSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository, String identifier)
        throws LivingDocServerException {
        log.debug("Retrieving SystemUnderTest: " + systemUnderTest.getName());
        XmlRpcMethodName methodName = XmlRpcMethodName.getSystemUnderTest;
        GetSystemUnderTestRequest request = new GetSystemUnderTestRequest(systemUnderTest, repository);
        GetSystemUnderTestResponse response = exchangeRest(methodName, request, GetSystemUnderTestResponse.class);
        return response.systemUnderTest;
    }

    @Override
    public void createSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository, String identifier)
        throws LivingDocServerException {
        log.debug("Creating SystemUnderTest: " + systemUnderTest.getName());
        XmlRpcMethodName methodName = XmlRpcMethodName.createSystemUnderTest;
        CreateSystemUnderTestRequest request = new CreateSystemUnderTestRequest(systemUnderTest, repository);
        exchangeRest(methodName, request, Void.class);
    }

    @Override
    public void updateSystemUnderTest(String oldSystemUnderTestName, SystemUnderTest newSystemUnderTest,
        Repository repository, String identifier) throws LivingDocServerException {
        log.debug("Updating SystemUnderTest: " + oldSystemUnderTestName);
        XmlRpcMethodName methodName = XmlRpcMethodName.updateSystemUnderTest;
        UpdateSystemUnderTestRequest request =
            new UpdateSystemUnderTestRequest(oldSystemUnderTestName, newSystemUnderTest, repository);
        exchangeRest(methodName, request, Void.class);
    }

    @Override
    public void removeSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository, String identifier)
        throws LivingDocServerException {
        log.debug("Removing SystemUnderTest: " + systemUnderTest.getName());
        XmlRpcMethodName methodName = XmlRpcMethodName.removeSystemUnderTest;
        RemoveSystemUnderTestRequest request = new RemoveSystemUnderTestRequest(systemUnderTest, repository);
        exchangeRest(methodName, request, Void.class);
    }

    @Override
    public void setSystemUnderTestAsDefault(SystemUnderTest systemUnderTest, Repository repository, String identifier)
        throws LivingDocServerException {
        log.debug("Setting as default the SystemUnderTest: " + systemUnderTest.getName());
        XmlRpcMethodName methodName = XmlRpcMethodName.setSystemUnderTestAsDefault;
        SetSystemUnderTestAsDefaultRequest request = new SetSystemUnderTestAsDefaultRequest(systemUnderTest, repository);
        exchangeRest(methodName, request, Void.class);
    }

    @Override
    public void removeRequirement(Requirement requirement, String identifier) throws LivingDocServerException {
        log.debug("Removing Requirement: " + requirement.getName());
        XmlRpcMethodName methodName = XmlRpcMethodName.removeRequirement;
        RemoveRequirementRequest request = new RemoveRequirementRequest(requirement);
        exchangeRest(methodName, request, Void.class);
    }

    @Override
    public Specification getSpecification(Specification specification, String identifier) throws LivingDocServerException {
        log.debug("Retrieving Specification: " + specification.getName());
        XmlRpcMethodName methodName = XmlRpcMethodName.getSpecification;
        GetSpecificationRequest request = new GetSpecificationRequest(specification);
        GetSpecificationResponse response = exchangeRest(methodName, request, GetSpecificationResponse.class);
        return response.specification;
    }

    @Override
    public Specification createSpecification(Specification specification, String identifier)
        throws LivingDocServerException {
        log.debug("Creating Specification: " + specification.getName());
        XmlRpcMethodName methodName = XmlRpcMethodName.createSpecification;
        CreateSpecificationRequest request = new CreateSpecificationRequest(specification);
        CreateSpecificationResponse response = exchangeRest(methodName, request, CreateSpecificationResponse.class);
        return response.specification;
    }

    @Override
    public void updateSpecification(Specification oldSpecification, Specification newSpecification, String identifier)
        throws LivingDocServerException {
        log.debug("Updating Specification: " + oldSpecification.getName());
        XmlRpcMethodName methodName = XmlRpcMethodName.updateSpecification;
        UpdateSpecificationRequest request = new UpdateSpecificationRequest(oldSpecification, newSpecification);
        exchangeRest(methodName, request, Void.class);
    }

    @Override
    public void removeSpecification(Specification specification, String identifier) throws LivingDocServerException {
        log.debug("Removing Specification: " + specification.getName());
        XmlRpcMethodName methodName = XmlRpcMethodName.removeSpecification;
        RemoveSpecificationRequest request = new RemoveSpecificationRequest(specification);
        exchangeRest(methodName, request, Void.class);
    }

    @Override
    public void createReference(Reference reference, String identifier) throws LivingDocServerException {
        log.debug(
            "Creating Reference: " + reference.getRequirement().getName() + "," + reference.getSpecification().getName());
        XmlRpcMethodName methodName = XmlRpcMethodName.createReference;
        CreateReferenceRequest request = new CreateReferenceRequest(reference);
        exchangeRest(methodName, request, Void.class);
    }

    @Override
    public Reference updateReference(Reference oldReference, Reference newReference, String identifier)
        throws LivingDocServerException {
        log.debug("Updating Reference: " + newReference.getRequirement().getName() + "," + newReference.getSpecification()
            .getName());
        XmlRpcMethodName methodName = XmlRpcMethodName.updateReference;
        UpdateReferenceRequest request = new UpdateReferenceRequest(oldReference, newReference);
        UpdateReferenceResponse response = exchangeRest(methodName, request, UpdateReferenceResponse.class);
        return response.reference;
    }

    @Override
    public void removeReference(Reference reference, String identifier) throws LivingDocServerException {
        log.debug(
            "Removing Reference: " + reference.getRequirement().getName() + "," + reference.getSpecification().getName());
        XmlRpcMethodName methodName = XmlRpcMethodName.removeReference;
        RemoveReferenceRequest request = new RemoveReferenceRequest(reference);
        exchangeRest(methodName, request, Void.class);
    }

    @Override
    public Execution runSpecification(SystemUnderTest systemUnderTest, Specification specification,
        boolean implementedVersion, String locale, String identifier) throws LivingDocServerException {
        log.debug("Running Specification: " + specification.getName() + " ON System:" + systemUnderTest.getName());
        XmlRpcMethodName methodName = XmlRpcMethodName.runSpecification;
        RunSpecificationRequest request =
            new RunSpecificationRequest(systemUnderTest, specification, implementedVersion, locale);
        RunSpecificationResponse response = exchangeRest(methodName, request, RunSpecificationResponse.class);
        return response.execution;
    }

    @Override
    public Reference runReference(Reference reference, String locale, String identifier) throws LivingDocServerException {
        log.debug(
            "Running Reference: " + reference.getRequirement().getName() + "," + reference.getSpecification().getName());
        XmlRpcMethodName methodName = XmlRpcMethodName.runReference;
        RunReferenceRequest request = new RunReferenceRequest(reference, locale);
        RunReferenceResponse response = exchangeRest(methodName, request, RunReferenceResponse.class);
        return response.reference;
    }

    @Override
    public RequirementSummary getSummary(Requirement requirement, String identifier) throws LivingDocServerException {
        log.debug("Getting Requirement " + requirement.getName() + " summary");
        XmlRpcMethodName methodName = XmlRpcMethodName.getRequirementSummary;
        GetSummaryRequest request = new GetSummaryRequest(requirement);
        GetSummaryResponse response = exchangeRest(methodName, request, GetSummaryResponse.class);
        return response.requirementSummary;
    }

    @Override
    public DocumentNode getSpecificationHierarchy(Repository repository, SystemUnderTest systemUnderTest, String identifier)
        throws LivingDocServerException {
        log.debug("Get Specification Hierarchy: " + repository.getName() + " & " + systemUnderTest.getName());
        XmlRpcMethodName methodName = XmlRpcMethodName.getSpecificationHierarchy;
        GetSpecificationHierarchyRequest request = new GetSpecificationHierarchyRequest(repository, systemUnderTest);
        GetSpecificationHierarchyResponse response =
            exchangeRest(methodName, request, GetSpecificationHierarchyResponse.class);
        return response.documentNode;
    }

    @Override
    public ServerPropertiesManager getServerPropertiesManager() {
        // TODO
        throw new NotImplementedException("not implemented: getServerPropertiesManager");
    }

    private <T> T exchangeRest(XmlRpcMethodName methodName, Object request, Class<T> responseType)
        throws LivingDocServerException {

        RequestEntity<Object> requestEntity = RequestEntity.post(getUri())
            .contentType(MediaType.APPLICATION_JSON)
            .header("method-name", methodName.name())
            .body(request);
        ResponseEntity<T> responseEntity = template.exchange(requestEntity, responseType);

        HttpStatus statusCode = responseEntity.getStatusCode();
        if (!HttpStatus.OK.equals(statusCode)) {
            throw new LivingDocServerException(LivingDocServerErrorKey.CALL_FAILED,
                "call was not successful, status: " + statusCode);
        }
        return responseEntity.getBody();

    }

    private URI getUri() throws LivingDocServerException {
        try {
            String url = baseUrl;
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            return new URI(url += "/rest/livingdoc/1.0/command");
        } catch (URISyntaxException e) {
            throw new LivingDocServerException(e);
        }
    }

}
