package info.novatec.testit.livingdoc.server.rpc;

import java.util.Set;

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

import javax.annotation.Nullable;


/**
 * The Client interface. All available methods are documented here.
 * <p>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 */
public interface RpcClientService {

    /**
     * Tests the connection at the url and handler.
     * <p>
     *
     * @param url Could be null.
     * @param handler Could be null.
     * @return true if server successfully pinged.
     * @throws LivingDocServerException
     */
    boolean testConnection(@Nullable String url, @Nullable String handler) throws LivingDocServerException;

    /**
     * Pings the server.
     * <p>
     *
     * @param repository
     * @param identifier
     * @return true if server successfully pinged.
     * @throws LivingDocServerException
     */
    boolean ping(Repository repository, String identifier) throws LivingDocServerException;

    /**
     * Retrieves the Runner for a given name.
     * <p>
     *
     * @param name
     * @param identifier
     * @return the Runner for a given name.
     * @throws LivingDocServerException
     */
    Runner getRunner(String name, String identifier) throws LivingDocServerException;

    /**
     * Retrieves the Runners available.
     * <p>
     *
     * @param identifier
     * @return the all Runners available.
     * @throws LivingDocServerException
     */
    Set<Runner> getAllRunners(String identifier) throws LivingDocServerException;

    /**
     * Creates a new Runner
     * <p>
     *
     * @param runner
     * @param identifier
     * @throws LivingDocServerException
     */
    void createRunner(Runner runner, String identifier) throws LivingDocServerException;

    /**
     * Updates the Runner
     * <p>
     *
     * @param oldRunnerName
     * @param runner
     * @param identifier
     * @throws LivingDocServerException
     */
    void updateRunner(String oldRunnerName, Runner runner, String identifier) throws LivingDocServerException;

    /**
     * Removes the Runner of the given name
     * <p>
     *
     * @param name
     * @param identifier
     * @throws LivingDocServerException
     */
    void removeRunner(String name, String identifier) throws LivingDocServerException;

    /**
     * Checks if registered and retrieves the Repository.
     * <p>
     *
     * @param repository
     * @param identifier
     * @return the registered Repository.
     * @throws LivingDocServerException
     */
    Repository getRegisteredRepository(Repository repository, String identifier) throws LivingDocServerException;

    /**
     * Registers the repository in the server. If project not found it will be
     * created.
     *
     * @param repository
     * @param identifier
     * @return the registered repository.
     * @throws LivingDocServerException
     */
    Repository registerRepository(Repository repository, String identifier) throws LivingDocServerException;

    /**
     * Updates the Repository Registration. If project not found it will be
     * created.
     * <p>
     *
     * @param repository
     * @param identifier
     * @throws LivingDocServerException
     */
    void updateRepositoryRegistration(Repository repository, String identifier) throws LivingDocServerException;

    /**
     * Removes the Repository if this one does not hold any specifications.
     * <p>
     *
     * @param repositoryUid
     * @param identifier
     * @throws LivingDocServerException
     */
    void removeRepository(String repositoryUid, String identifier) throws LivingDocServerException;

    /**
     * Retrieves the complete project list.
     * <p>
     *
     * @param identifier
     * @return the complete project list.
     * @throws LivingDocServerException
     */
    Set<Project> getAllProjects(String identifier) throws LivingDocServerException;

    /**
     * Retrieves the Specification repository list for the project associated
     * with the specified repository or an error id in a HashTable if an error
     * occurred.
     * <p>
     *
     * @param repository
     * @param identifier
     * @return the Specification repository list for the project associated with
     * the specified repository or an error id in a HashTable if an error
     * occurred.
     * @throws LivingDocServerException
     */
    Set<Repository> getSpecificationRepositoriesOfAssociatedProject(Repository repository, String identifier)
        throws LivingDocServerException;

    /**
     * Retrieves the Specification repository list for the project associated
     * with the specified system under test or an error id in a HashTable if an
     * error occurred.
     * <p>
     *
     * @param systemUnderTest
     * @param identifier
     * @return the Specification repository list for the project associated with
     * the specified systemUnderTest or an error id in a HashTable if an error
     * occurred.
     * @throws LivingDocServerException
     */
    Set<Repository> getSpecificationRepositoriesOfAssociatedProject(SystemUnderTest systemUnderTest, String identifier)
        throws LivingDocServerException;

    /**
     * Retrieves all the Specification repositories list by project or an error
     * id in a HashTable if an error occurred.
     * <p>
     *
     * @param identifier
     * @return the Specification repository list for the project or an error id
     * in a HashTable if an error occurred.
     * @throws LivingDocServerException
     */
    Set<Repository> getAllSpecificationRepositories(String identifier) throws LivingDocServerException;

    /**
     * Retrieves the Repository list for the project associated with the
     * specified system under test or an error id in a HashTable if an error
     * occurred.
     * <p>
     *
     * @param systemUnderTest
     * @param identifier
     * @return the repository list for the project associated with the specified
     * systemUnderTest or an error id in a HashTable if an error occurred.
     * @throws LivingDocServerException
     */
    Set<Repository> getAllRepositoriesForSystemUnderTest(SystemUnderTest systemUnderTest, String identifier)
        throws LivingDocServerException;

    /**
     * Retrieves the Requirement repository list for the project associated with
     * the specified repository or an error id in a HashTable if an error
     * occurred.
     * <p>
     *
     * @param repository
     * @param identifier
     * @return the Requirement repository list for the project associated with
     * the specified repository or an error id in a HashTable if an error
     * occurred.
     * @throws LivingDocServerException
     */
    Set<Repository> getRequirementRepositoriesOfAssociatedProject(Repository repository, String identifier)
        throws LivingDocServerException;

    /**
     * Retrieves the SystemUnderTest list for the project associated with the
     * specified repository or an error id in a HashTable if an error occurred.
     * <p>
     *
     * @param repository
     * @param identifier
     * @return the SystemUnderTest list for for the project associated with the
     * specified repository or an error id in a HashTable if an error occurred.
     * @throws LivingDocServerException
     */
    Set<SystemUnderTest> getSystemUnderTestsOfAssociatedProject(Repository repository, String identifier)
        throws LivingDocServerException;

    /**
     * Retrieves the SystemUnderTest list for the project associated.
     * <p>
     *
     * @param projectName
     * @param identifier
     * @return the SystemUnderTest list for for the project.
     * @throws LivingDocServerException
     */
    Set<SystemUnderTest> getSystemUnderTestsOfProject(String projectName, String identifier) throws LivingDocServerException;

    /**
     * Adds the SystemUnderTest to the SystemUnderTest list of the
     * Specification.
     * <p>
     *
     * @param systemUnderTest
     * @param specification
     * @param identifier
     * @throws LivingDocServerException
     */
    void addSystemUnderTest(SystemUnderTest systemUnderTest, Specification specification, String identifier)
        throws LivingDocServerException;

    /**
     * Removes the SystemUnderTest to the SystemUnderTest list of the
     * Specification.
     * <p>
     *
     * @param systemUnderTest
     * @param specification
     * @param identifier
     * @throws LivingDocServerException
     */
    void removeSystemUnderTest(SystemUnderTest systemUnderTest, Specification specification, String identifier)
        throws LivingDocServerException;

    /**
     * Checks if the Specification is in at least one Reference.
     * <p>
     *
     * @param specification
     * @param identifier
     * @return true if the specification is in at least one Reference.
     * @throws LivingDocServerException
     */
    boolean hasReferences(Specification specification, String identifier) throws LivingDocServerException;

    /**
     * Retrieves the References list of the specified Specification
     * <p>
     *
     * @param specification
     * @param identifier
     * @return the References list of the specified Specification
     * @throws LivingDocServerException
     */
    Set<Reference> getReferences(Specification specification, String identifier) throws LivingDocServerException;

    /**
     * Checks if the Requirement is in at least one Reference.
     * <p>
     *
     * @param requirement
     * @param identifier
     * @return true if the Requirement is in at least one Reference.
     * @throws LivingDocServerException
     */
    boolean hasReferences(Requirement requirement, String identifier) throws LivingDocServerException;

    /**
     * Retrieves the References list of the specified requirement
     * <p>
     *
     * @param requirement
     * @param identifier
     * @return the References list of the specified requirement
     * @throws LivingDocServerException
     */
    Set<Reference> getReferences(Requirement requirement, String identifier) throws LivingDocServerException;

    /**
     * Retrieves the Reference.
     * <p>
     *
     * @param reference
     * @param identifier
     * @return the Reference.
     * @throws LivingDocServerException
     */
    Reference getReference(Reference reference, String identifier) throws LivingDocServerException;

    /**
     * Creates a new SystemUnderTest.
     * <p>
     *
     * @param systemUnderTest
     * @param repository
     * @param identifier
     * @throws LivingDocServerException
     */
    void createSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository, String identifier)
        throws LivingDocServerException;

    /**
     * Retrieves the SystemUnderTest.
     * <p>
     *
     * @param systemUnderTest
     * @param repository
     * @param identifier
     * @return SystemUnderTest
     * @throws LivingDocServerException
     */
    SystemUnderTest getSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository, String identifier)
        throws LivingDocServerException;

    /**
     * Updates the SystemUnderTest.
     * <p>
     *
     * @param oldSystemUnderTestName
     * @param newSystemUnderTest
     * @param repository
     * @param identifier
     * @throws LivingDocServerException
     */
    void updateSystemUnderTest(String oldSystemUnderTestName, SystemUnderTest newSystemUnderTest, Repository repository,
        String identifier) throws LivingDocServerException;

    /**
     * Removes the SystemUnderTest.
     * <p>
     *
     * @param systemUnderTest
     * @param repository
     * @param identifier
     * @throws LivingDocServerException
     */
    void removeSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository, String identifier)
        throws LivingDocServerException;

    /**
     * Sets the systemUnderTest as the project default SystemUnderTest
     * <p>
     *
     * @param systemUnderTest
     * @param repository
     * @param identifier
     * @throws LivingDocServerException
     */
    void setSystemUnderTestAsDefault(SystemUnderTest systemUnderTest, Repository repository, String identifier)
        throws LivingDocServerException;

    /**
     * Removes the Requirement
     * <p>
     *
     * @param requirement
     * @param identifier
     * @throws LivingDocServerException
     */
    void removeRequirement(Requirement requirement, String identifier) throws LivingDocServerException;

    /**
     * Retrieves the specification
     * <p>
     *
     * @param specification
     * @param identifier
     * @return the specification
     * @throws LivingDocServerException
     */
    Specification getSpecification(Specification specification, String identifier) throws LivingDocServerException;

    /**
     * Creates the Specification
     * <p>
     *
     * @param specification
     * @param identifier
     * @return the new Specification
     * @throws LivingDocServerException
     */
    Specification createSpecification(Specification specification, String identifier) throws LivingDocServerException;

    /**
     * Updates the Specification
     * <p>
     *
     * @param oldSpecification
     * @param newSpecification
     * @param identifier
     * @throws LivingDocServerException
     */
    void updateSpecification(Specification oldSpecification, Specification newSpecification, String identifier)
        throws LivingDocServerException;

    /**
     * Removes the Specification
     * <p>
     *
     * @param specification
     * @param identifier
     * @throws LivingDocServerException
     */
    void removeSpecification(Specification specification, String identifier) throws LivingDocServerException;

    /**
     * Creates a Reference
     * <p>
     *
     * @param reference
     * @param identifier
     * @throws LivingDocServerException
     */
    void createReference(Reference reference, String identifier) throws LivingDocServerException;

    /**
     * Update the Reference. The Old one will be deleted based on the
     * oldReferenceParams and a new One will be created based on the
     * newReferenceParams.
     * <p>
     *
     * @param oldReference
     * @param newReference
     * @param identifier
     * @return the updated Reference.
     * @throws LivingDocServerException
     */
    Reference updateReference(Reference oldReference, Reference newReference, String identifier)
        throws LivingDocServerException;

    /**
     * Deletes the specified Reference.
     * <p>
     *
     * @param reference
     * @param identifier
     * @throws LivingDocServerException
     */
    void removeReference(Reference reference, String identifier) throws LivingDocServerException;

    /**
     * Executes the Specification over the selected SystemUnderTest.
     * <p>
     *
     * @param systemUnderTest
     * @param specification
     * @param implementedVersion
     * @param locale
     * @param identifier
     * @return the Execution of the Specification over the selected
     * SystemUnderTest.
     * @throws LivingDocServerException
     */
    Execution runSpecification(SystemUnderTest systemUnderTest, Specification specification, boolean implementedVersion,
        String locale, String identifier) throws LivingDocServerException;

    /**
     * Executes the Reference.
     * <p>
     *
     * @param reference
     * @param locale
     * @param identifier
     * @return the Reference with its last execution.
     * @throws LivingDocServerException
     */
    Reference runReference(Reference reference, String locale, String identifier) throws LivingDocServerException;

    /**
     * Retrieves the list of specification
     * <p>
     *
     * @param repository
     * @param systemUnderTest
     * @param identifier
     * @return
     */
    DocumentNode getSpecificationHierarchy(Repository repository, SystemUnderTest systemUnderTest, String identifier)
        throws LivingDocServerException;

    /**
     * Retrieves the requirement summary.
     * <p>
     *
     * @param requirement
     * @param identifier
     * @return the requirement summary.
     */
    RequirementSummary getSummary(Requirement requirement, String identifier) throws LivingDocServerException;

    /**
     * Retrieves the server properties manager.
     * <p>
     *
     * @return the server properties manager.
     *
     * @deprecated This method is not necessary anymore since the service implementation with REST.<br>
     * Could be removed in the next version.
     */
    @Deprecated
    ServerPropertiesManager getServerPropertiesManager();
}
