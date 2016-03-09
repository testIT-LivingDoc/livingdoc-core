package info.novatec.testit.livingdoc.server.rpc;

import java.util.List;


/**
 * The XmlRpcService provides an XML-RPC interface into LivingDoc Server. All
 * available methods are documented here.
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 * 
 * @author JCHUET
 */
public interface RpcServerService {
    String SERVICE_HANDLER = "livingdoc1";

    /**
     * Test the connection to the server.
     * <p/>
     * 
     * @return success.
     */
    String testConnection();

    /**
     * Pings the server.
     * <p/>
     * 
     * @param repositoryParams
     * @return success.
     */
    String ping(List<Object> repositoryParams);

    /**
     * Retrieves the runner for a given the name.
     * <p/>
     * 
     * @param name
     * @return the runner for a given the name.
     */
    List<Object> getRunner(String name);

    /**
     * Retrieves all available Runners.
     * <p/>
     * 
     * @return all available Runners
     */
    List<Object> getAllRunners();

    /**
     * Creates a new Runner.
     * <p/>
     * 
     * @param runnerParams
     * @return error id if an error occured
     */
    String createRunner(List<Object> runnerParams);

    /**
     * Updates the Runner.
     * <p/>
     * 
     * @param oldRunnerName
     * @param runnerParams
     * @return error id if an error occured
     */
    String updateRunner(String oldRunnerName, List<Object> runnerParams);

    /**
     * Creates a new Runner.
     * <p/>
     * 
     * @param name
     * @return error id if an error occured
     */
    String removeRunner(String name);

    /**
     * Retrieves the Repository for the uid.
     * <p/>
     * 
     * @param repositoryParams
     * @return the Repository for the uid.
     */
    List<Object> getRegisteredRepository(List<Object> repositoryParams);

    /**
     * Registers the repository in LivingDoc-server. If project not found it
     * will be created.
     * <p/>
     * 
     * @param repositoryParams
     * @return the registered repository.
     */
    List<Object> registerRepository(List<Object> repositoryParams);

    /**
     * Updates the Repository Registration. If project not found it will be
     * created.
     * <p/>
     * 
     * @param repositoryParams
     */
    String updateRepositoryRegistration(List<Object> repositoryParams);

    /**
     * Removes the Repository if this one doesnt hold any specifications.
     * <p/>
     * 
     * @param repositoryParams
     */
    String removeRepository(String repositoryUid);

    /**
     * Retrieves the complete project list.
     * <p/>
     * 
     * @return the complete project list.
     */
    List<Object> getAllProjects();

    /**
     * Retrieves all the Specification repository grouped by project or an error
     * id in a Hastable if an error occured.
     * <p/>
     * 
     * @return the Specification repository list grouped by types for the
     * project or an error id in a Hastable if an error occured.
     */
    List<Object> getAllSpecificationRepositories();

    /**
     * Retrieves the Specification repository list grouped by types for the
     * project associated with the specified repository or an error id in a
     * Hastable if an error occured.
     * <p/>
     * 
     * @param repositoryParams
     * @return the Specification repository list grouped by types for the
     * project associated with the specified repository or an error id in a
     * Hastable if an error occured.
     */
    List<Object> getSpecificationRepositoriesOfAssociatedProject(List<Object> repositoryParams);

    /**
     * Retrieves the Repository list for the project associated with the
     * specified system under test or an error id in a Hastable if an error
     * occured.
     * <p/>
     * 
     * @param systemUnderTestParams
     * @return the repository list for the project associated with the specified
     * systemUnderTest or an error id in a Hastable if an error occured.
     */
    List<Object> getAllRepositoriesForSystemUnderTest(List<Object> systemUnderTestParams);

    /**
     * Retrieves the Specification repository list grouped by types for the
     * project associated with the specified SystemUnderTest or an error id in a
     * Hastable if an error occured.
     * <p/>
     * 
     * @param systemUnderTestParams
     * @return the Specification repository list grouped by types for the
     * project associated with the specified SystemUnderTest or an error id in a
     * Hastable if an error occured.
     */
    List<Object> getSpecificationRepositoriesForSystemUnderTest(List<Object> systemUnderTestParams);

    /**
     * Retrieves the Requirement repository list for the project associated with
     * the specified repository or an error id in a Hastable if an error
     * occured.
     * <p/>
     * 
     * @param repositoryParams
     * @return the Requirement repository list for the project associated with
     * the specified repository or an error id in a Hastable if an error
     * occured.
     */
    List<Object> getRequirementRepositoriesOfAssociatedProject(List<Object> repositoryParams);

    /**
     * Retrieves the SystemUnderTest list for the project associated with the
     * specified repository or an error id in a Hastable if an error occured.
     * <p/>
     * 
     * @param repositoryParams
     * @return the SystemUnderTest list for the project associated with the
     * specified repository or an error id in a Hastable if an error occured.
     */
    List<Object> getSystemUnderTestsOfAssociatedProject(List<Object> repositoryParams);

    /**
     * Retrieves the SystemUnderTest list for the project associated or an error
     * id in a Hastable if an error occured.
     * <p/>
     * 
     * @param projectName
     * @return the SystemUnderTest list for the project associated or an error
     * id in a Hastable if an error occured.
     */
    List<Object> getSystemUnderTestsOfProject(String projectName);

    /**
     * Adds the SystemUnderTest to the SystemUnderTest list of the
     * Specification.
     * <p/>
     * 
     * @param systemUnderTestParams
     * @param specificationParams
     * @return error id if an error occured
     */
    String addSpecificationSystemUnderTest(List<Object> systemUnderTestParams, List<Object> specificationParams);

    /**
     * Removes the SystemUnderTest to the SystemUnderTest list of the
     * Specification.
     * <p/>
     * 
     * @param systemUnderTestParams
     * @param specificationParams
     * @return error id if an error occured
     */
    String removeSpecificationSystemUnderTest(List<Object> systemUnderTestParams,
        List<Object> specificationParams);

    /**
     * Checks if the Specification is in atleast one reference.
     * <p/>
     * 
     * @param specificationParams
     * @return true if the Specification is in atleast one reference.
     */
    String doesSpecificationHasReferences(List<Object> specificationParams);

    /**
     * Retrieves the references list of the specified Specification
     * <p/>
     * 
     * @param specificationParams
     * @return the references list of the specified Specification
     */
    List<Object> getSpecificationReferences(List<Object> specificationParams);

    /**
     * Checks if the Requirement is in atleast one Reference.
     * <p/>
     * 
     * @param requirementParams
     * @return true if the Requirement is in atleast one Reference.
     */
    String doesRequirementHasReferences(List<Object> requirementParams);

    /**
     * Retrieves the References list of the specified requirement
     * <p/>
     * 
     * @param requirementParams
     * @return the References list of the specified requirement
     */
    List<Object> getRequirementReferences(List<Object> requirementParams);

    /**
     * Retrieves the Reference.
     * </p>
     * 
     * @param referenceParams
     * @return the Reference.
     */
    List<Object> getReference(List<Object> referenceParams);

    /**
     * Retrieves the systemUnderTest
     * <p/>
     * 
     * @param systemUnderTestParams
     * @param repositoryParams
     * @return error id if an error occured
     */
    List<Object> getSystemUnderTest(List<Object> systemUnderTestParams, List<Object> repositoryParams);

    /**
     * Creates the systemUnderTest
     * <p/>
     * 
     * @param systemUnderTestParams
     * @param repositoryParams
     * @return error id if an error occured
     */
    String createSystemUnderTest(List<Object> systemUnderTestParams, List<Object> repositoryParams);

    /**
     * Updates the systemUnderTest
     * <p/>
     * 
     * @param oldSystemUnderTestName
     * @param systemUnderTestParams
     * @param repositoryParams
     * @return error id if an error occured
     */
    String updateSystemUnderTest(String oldSystemUnderTestName, List<Object> systemUnderTestParams,
                                 List<Object> repositoryParams);

    /**
     * Removes the systemUnderTest
     * <p/>
     * 
     * @param systemUnderTestParams
     * @param repositoryParams
     * @return error id if an error occured
     */
    String removeSystemUnderTest(List<Object> systemUnderTestParams, List<Object> repositoryParams);

    /**
     * Sets the systemUnderTest as the project default SystemUnderTest
     * <p/>
     * 
     * @param systemUnderTestParams
     * @param repositoryParams
     * @return error id if an error occured
     */
    String setSystemUnderTestAsDefault(List<Object> systemUnderTestParams, List<Object> repositoryParams);

    /**
     * Removes the Requirement.
     * <p/>
     * 
     * @param requirementParams
     * @return error id if an error occured
     */
    String removeRequirement(List<Object> requirementParams);

    /**
     * Retrieves the Specification
     * <p/>
     * 
     * @param specificationParams
     * @return the Specification
     */
    List<Object> getSpecification(List<Object> specificationParams);

    /**
     * Retrieves all Specifications for a given SystemUnderTest and Repository
     * <p>
     * 
     * @param systemUnderTestParams
     * @param repositoryParams
     * @return all Specifications for a given SystemUnderTest and Repository
     */
    List<Object> getSpecifications(List<Object> systemUnderTestParams, List<Object> repositoryParams);

    /**
     * Retrieves the Specification location list for a given SystemUnderTest and
     * Repository
     * <p/>
     * 
     * @param repositoryUID
     * @param systemUnderTestName
     * @return the Specification location list for a given SystemUnderTest and
     * Repository
     */
    List< ? > getListOfSpecificationLocations(String repositoryUID, String systemUnderTestName);

    /**
     * Creates the Specification
     * <p/>
     * 
     * @param specificationParams
     * @return the new Specification
     */
    List<Object> createSpecification(List<Object> specificationParams);

    /**
     * Updates the Specification.
     * <p/>
     * 
     * @param oldSpecificationParams
     * @param newSpecificationParams
     * @return error id if an error occured
     */
    String updateSpecification(List<Object> oldSpecificationParams, List<Object> newSpecificationParams);

    /**
     * Removes the Specification.
     * <p/>
     * 
     * @param specificationParams
     * @return error id if an error occured
     */
    String removeSpecification(List<Object> specificationParams);

    /**
     * Creates a Reference
     * <p/>
     * 
     * @param referenceParams
     * @return error id if an error occured
     */
    String createReference(List<Object> referenceParams);

    /**
     * Update the Reference. The Old one will be deleted based on the
     * oldReferenceParams and a new One will be created based on the
     * newReferenceParams.
     * <p/>
     * 
     * @param oldReferenceParams
     * @param newReferenceParams
     * @return the updated Reference
     */
    List<Object> updateReference(List<Object> oldReferenceParams, List<Object> newReferenceParams);

    /**
     * Deletes the specified Reference.
     * <p/>
     * 
     * @param referenceParams
     * @return error id if an eror occured
     */
    String removeReference(List<Object> referenceParams);

    /**
     * Executes the Specification over the selected SystemUnderTest.
     * <p/>
     * 
     * @param systemUnderTestParams
     * @param specificationParams
     * @param implementedVersion
     * @param locale
     * @return the Execution of the Specification over the selected
     * SystemUnderTest.
     */
    List<Object> runSpecification(List<Object> systemUnderTestParams, List<Object> specificationParams,
        boolean implementedVersion, String locale);

    /**
     * Executes the Reference.
     * <p/>
     * 
     * @param referenceParams
     * @param locale
     * @return the Reference executed
     */
    List<Object> runReference(List<Object> referenceParams, String locale);

    /**
     * Retrieves the Requirement summary.
     * <p/>
     * 
     * @param requirementParams
     * @return the Requirement summary.
     */
    List<Object> getRequirementSummary(List<Object> requirementParams);

    /**
     * Retrieve the spcifications hierarchy for a Repository.
     * <p/>
     * 
     * @param repositoryParams
     * @param sutParams
     * @return the TestCase executed
     */
    List<Object> getSpecificationHierarchy(List<Object> repositoryParams, List<Object> sutParams);

}
