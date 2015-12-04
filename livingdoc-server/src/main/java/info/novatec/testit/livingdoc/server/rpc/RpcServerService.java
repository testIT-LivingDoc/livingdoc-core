package info.novatec.testit.livingdoc.server.rpc;

import java.util.Vector;


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
    public String testConnection();

    /**
     * Pings the server.
     * <p/>
     * 
     * @param repositoryParams
     * @return success.
     */
    public String ping(Vector<Object> repositoryParams);

    /**
     * Retrieves the runner for a given the name.
     * <p/>
     * 
     * @param name
     * @return the runner for a given the name.
     */
    public Vector<Object> getRunner(String name);

    /**
     * Retrieves all available Runners.
     * <p/>
     * 
     * @return all available Runners
     */
    public Vector<Object> getAllRunners();

    /**
     * Creates a new Runner.
     * <p/>
     * 
     * @param runnerParams
     * @return error id if an error occured
     */
    public String createRunner(Vector<Object> runnerParams);

    /**
     * Updates the Runner.
     * <p/>
     * 
     * @param oldRunnerName
     * @param runnerParams
     * @return error id if an error occured
     */
    public String updateRunner(String oldRunnerName, Vector<Object> runnerParams);

    /**
     * Creates a new Runner.
     * <p/>
     * 
     * @param name
     * @return error id if an error occured
     */
    public String removeRunner(String name);

    /**
     * Retrieves the Repository for the uid.
     * <p/>
     * 
     * @param repositoryParams
     * @return the Repository for the uid.
     */
    public Vector<Object> getRegisteredRepository(Vector<Object> repositoryParams);

    /**
     * Registers the repository in LivingDoc-server. If project not found it
     * will be created.
     * <p/>
     * 
     * @param repositoryParams
     * @return the registered repository.
     */
    public Vector<Object> registerRepository(Vector<Object> repositoryParams);

    /**
     * Updates the Repository Registration. If project not found it will be
     * created.
     * <p/>
     * 
     * @param repositoryParams
     */
    public String updateRepositoryRegistration(Vector<Object> repositoryParams);

    /**
     * Removes the Repository if this one doesnt hold any specifications.
     * <p/>
     * 
     * @param repositoryParams
     */
    public String removeRepository(String repositoryUid);

    /**
     * Retrieves the complete project list.
     * <p/>
     * 
     * @return the complete project list.
     */
    public Vector<Object> getAllProjects();

    /**
     * Retrieves all the Specification repository grouped by project or an error
     * id in a Hastable if an error occured.
     * <p/>
     * 
     * @return the Specification repository list grouped by types for the
     * project or an error id in a Hastable if an error occured.
     */
    public Vector<Object> getAllSpecificationRepositories();

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
    public Vector<Object> getSpecificationRepositoriesOfAssociatedProject(Vector<Object> repositoryParams);

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
    public Vector<Object> getAllRepositoriesForSystemUnderTest(Vector<Object> systemUnderTestParams);

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
    public Vector<Object> getSpecificationRepositoriesForSystemUnderTest(Vector<Object> systemUnderTestParams);

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
    public Vector<Object> getRequirementRepositoriesOfAssociatedProject(Vector<Object> repositoryParams);

    /**
     * Retrieves the SystemUnderTest list for the project associated with the
     * specified repository or an error id in a Hastable if an error occured.
     * <p/>
     * 
     * @param repositoryParams
     * @return the SystemUnderTest list for the project associated with the
     * specified repository or an error id in a Hastable if an error occured.
     */
    public Vector<Object> getSystemUnderTestsOfAssociatedProject(Vector<Object> repositoryParams);

    /**
     * Retrieves the SystemUnderTest list for the project associated or an error
     * id in a Hastable if an error occured.
     * <p/>
     * 
     * @param projectName
     * @return the SystemUnderTest list for the project associated or an error
     * id in a Hastable if an error occured.
     */
    public Vector<Object> getSystemUnderTestsOfProject(String projectName);

    /**
     * Adds the SystemUnderTest to the SystemUnderTest list of the
     * Specification.
     * <p/>
     * 
     * @param systemUnderTestParams
     * @param specificationParams
     * @return error id if an error occured
     */
    public String addSpecificationSystemUnderTest(Vector<Object> systemUnderTestParams, Vector<Object> specificationParams);

    /**
     * Removes the SystemUnderTest to the SystemUnderTest list of the
     * Specification.
     * <p/>
     * 
     * @param systemUnderTestParams
     * @param specificationParams
     * @return error id if an error occured
     */
    public String removeSpecificationSystemUnderTest(Vector<Object> systemUnderTestParams,
        Vector<Object> specificationParams);

    /**
     * Checks if the Specification is in atleast one reference.
     * <p/>
     * 
     * @param specificationParams
     * @return true if the Specification is in atleast one reference.
     */
    public String doesSpecificationHasReferences(Vector<Object> specificationParams);

    /**
     * Retrieves the references list of the specified Specification
     * <p/>
     * 
     * @param specificationParams
     * @return the references list of the specified Specification
     */
    public Vector<Object> getSpecificationReferences(Vector<Object> specificationParams);

    /**
     * Checks if the Requirement is in atleast one Reference.
     * <p/>
     * 
     * @param requirementParams
     * @return true if the Requirement is in atleast one Reference.
     */
    public String doesRequirementHasReferences(Vector<Object> requirementParams);

    /**
     * Retrieves the References list of the specified requirement
     * <p/>
     * 
     * @param requirementParams
     * @return the References list of the specified requirement
     */
    public Vector<Object> getRequirementReferences(Vector<Object> requirementParams);

    /**
     * Retrieves the Reference.
     * </p>
     * 
     * @param referenceParams
     * @return the Reference.
     */
    public Vector<Object> getReference(Vector<Object> referenceParams);

    /**
     * Retrieves the systemUnderTest
     * <p/>
     * 
     * @param systemUnderTestParams
     * @param repositoryParams
     * @return error id if an error occured
     */
    public Vector<Object> getSystemUnderTest(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams);

    /**
     * Creates the systemUnderTest
     * <p/>
     * 
     * @param systemUnderTestParams
     * @param repositoryParams
     * @return error id if an error occured
     */
    public String createSystemUnderTest(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams);

    /**
     * Updates the systemUnderTest
     * <p/>
     * 
     * @param oldSystemUnderTestName
     * @param systemUnderTestParams
     * @param repositoryParams
     * @return error id if an error occured
     */
    public String updateSystemUnderTest(String oldSystemUnderTestName, Vector<Object> systemUnderTestParams,
        Vector<Object> repositoryParams);

    /**
     * Removes the systemUnderTest
     * <p/>
     * 
     * @param systemUnderTestParams
     * @param repositoryParams
     * @return error id if an error occured
     */
    public String removeSystemUnderTest(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams);

    /**
     * Sets the systemUnderTest as the project default SystemUnderTest
     * <p/>
     * 
     * @param systemUnderTestParams
     * @param repositoryParams
     * @return error id if an error occured
     */
    public String setSystemUnderTestAsDefault(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams);

    /**
     * Removes the Requirement.
     * <p/>
     * 
     * @param requirementParams
     * @return error id if an error occured
     */
    public String removeRequirement(Vector<Object> requirementParams);

    /**
     * Retrieves the Specification
     * <p/>
     * 
     * @param specificationParams
     * @return the Specification
     */
    public Vector<Object> getSpecification(Vector<Object> specificationParams);

    /**
     * Retrieves all Specifications for a given SystemUnderTest and Repository
     * <p>
     * 
     * @param systemUnderTestParams
     * @param repositoryParams
     * @return all Specifications for a given SystemUnderTest and Repository
     */
    public Vector<Object> getSpecifications(Vector<Object> systemUnderTestParams, Vector<Object> repositoryParams);

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
    public Vector< ? > getListOfSpecificationLocations(String repositoryUID, String systemUnderTestName);

    /**
     * Creates the Specification
     * <p/>
     * 
     * @param specificationParams
     * @return the new Specification
     */
    public Vector<Object> createSpecification(Vector<Object> specificationParams);

    /**
     * Updates the Specification.
     * <p/>
     * 
     * @param oldSpecificationParams
     * @param newSpecificationParams
     * @return error id if an error occured
     */
    public String updateSpecification(Vector<Object> oldSpecificationParams, Vector<Object> newSpecificationParams);

    /**
     * Removes the Specification.
     * <p/>
     * 
     * @param specificationParams
     * @return error id if an error occured
     */
    public String removeSpecification(Vector<Object> specificationParams);

    /**
     * Creates a Reference
     * <p/>
     * 
     * @param referenceParams
     * @return error id if an error occured
     */
    public String createReference(Vector<Object> referenceParams);

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
    public Vector<Object> updateReference(Vector<Object> oldReferenceParams, Vector<Object> newReferenceParams);

    /**
     * Deletes the specified Reference.
     * <p/>
     * 
     * @param referenceParams
     * @return error id if an eror occured
     */
    public String removeReference(Vector<Object> referenceParams);

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
    public Vector<Object> runSpecification(Vector<Object> systemUnderTestParams, Vector<Object> specificationParams,
        boolean implementedVersion, String locale);

    /**
     * Executes the Reference.
     * <p/>
     * 
     * @param referenceParams
     * @param locale
     * @return the Reference executed
     */
    public Vector<Object> runReference(Vector<Object> referenceParams, String locale);

    /**
     * Retrieves the Requirement summary.
     * <p/>
     * 
     * @param requirementParams
     * @return the Requirement summary.
     */
    public Vector<Object> getRequirementSummary(Vector<Object> requirementParams);

    /**
     * Retrieve the spcifications hierarchy for a Repository.
     * <p/>
     * 
     * @param repositoryParams
     * @param sutParams
     * @return the TestCase executed
     */
    public Vector<Object> getSpecificationHierarchy(Vector<Object> repositoryParams, Vector<Object> sutParams);

}
