/* Copyright (c) 2008 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org. */
package info.novatec.testit.livingdoc.server;

import java.util.List;

import info.novatec.testit.livingdoc.report.XmlReport;
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
import info.novatec.testit.livingdoc.server.transfer.SpecificationLocation;


public interface LivingDocServerService {

    /**
     * Retrieves the runner for a given the name.
     *
     * @param name The name of the runner to retrieve
     * @return the runner for a given the name.
     */
    Runner getRunner(String name) throws LivingDocServerException;

    /**
     * Retrieves all available Runners.
     *
     * @return all available Runners
     */
    List<Runner> getAllRunners() throws LivingDocServerException;

    /**
     * Creates a new Runner.
     *
     * @param runner The runner to create
     */
    void createRunner(Runner runner) throws LivingDocServerException;

    /**
     * Updates the Runner.
     *
     * @param oldRunnerName The name of the old runner to be updated
     * @param runner The runner to update
     */
    void updateRunner(String oldRunnerName, Runner runner) throws LivingDocServerException;

    /**
     * Creates a new Runner.
     *
     * @param name The name of the runner to remove
     */
    void removeRunner(String name) throws LivingDocServerException;

    /**
     * Retrieves the Repository for the uid.
     *
     * @param uid The repository identifier
     * @param maxUsers The maximum user the repository should allow, null for no
     * check
     * @return the Repository for the uid.
     */
    Repository getRepository(String uid, Integer maxUsers) throws LivingDocServerException;

    /**
     * Retrieves the Repository for the uid.
     *
     * @param repository The repository
     * @return the Repository for the uid.
     */
    Repository getRegisteredRepository(Repository repository) throws LivingDocServerException;

    /**
     * Registers the repository in LivingDoc-server. If project not found it
     * will be created.
     *
     * @param repository The repository to be registered
     * @return the registered repository.
     */
    Repository registerRepository(Repository repository) throws LivingDocServerException;

    /**
     * Updates the Repository Registration. If project not found it will be
     * created.
     *
     * @param repository The repository to update
     */
    void updateRepositoryRegistration(Repository repository) throws LivingDocServerException;

    /**
     * Removes the Repository if this one does not hold any specifications.
     *
     * @param repositoryUid The repository identifier to be removed
     */
    void removeRepository(String repositoryUid) throws LivingDocServerException;

    /**
     * Gets all repository associated to the given project.
     *
     * @param projectName Name of the project
     * @return list of repository
     */
    List<Repository> getRepositoriesOfAssociatedProject(String projectName) throws LivingDocServerException;

    /**
     * Retrieves the project for a given the name.
     *
     * @param name The name of the project to retrieve
     * @return the project for a given the name.
     */
    Project getProject(String name) throws LivingDocServerException;

    /**
     * Creates a new Project.
     *
     * @param project The project to create
     * @return the newly created project instance
     * @throws LivingDocServerException Exception
     */
    Project createProject(Project project) throws LivingDocServerException;

    /**
     * Updates the Project.
     *
     * @param oldProjectName The name of the old project to be updated
     * @param project The project to update
     * @return the newly updated project instance
     * @throws LivingDocServerException Exception
     */
    Project updateProject(String oldProjectName, Project project) throws LivingDocServerException;

    /**
     * Retrieves the complete project list.
     *
     * @return the complete project list.
     */
    List<Project> getAllProjects() throws LivingDocServerException;

    /**
     * Retrieves all the Specification repository grouped by project or an error
     * id in a HashTable if an error occurred.
     *
     * @return the Specification repository list grouped by types for the
     * project or an error id in a HashTable if an error occurred.
     */
    List<Repository> getAllSpecificationRepositories() throws LivingDocServerException;

    /**
     * Retrieves the Specification repository list grouped by types for the
     * project associated with the specified repository or an error id in a
     * HashTable if an error occurred.
     * <p/>
     *
     * @param repositoryUid The repository identifier
     * @return the Specification repository list grouped by types for the
     * project associated with the specified repository or an error id in a
     * HashTable if an error occurred.
     */
    List<Repository> getSpecificationRepositoriesOfAssociatedProject(String repositoryUid) throws LivingDocServerException;

    /**
     * Retrieves the Repository list for the project associated with the
     * specified system under test or an error id in a HashTable if an error
     * occurred.
     *
     * @param sut The system under test to retrieve the list of repository
     * @return the repository list for the project associated with the specified
     * systemUnderTest
     */
    List<Repository> getAllRepositoriesForSystemUnderTest(SystemUnderTest sut) throws LivingDocServerException;

    /**
     * Retrieves the Specification repository list grouped by types for the
     * project associated with the specified SystemUnderTest or an error id in a
     * HashTable if an error occurred.
     *
     * @param sut The system under test to retrieve the list of repository
     * @return the Specification repository list grouped by types for the
     * project associated with the specified SystemUnderTest or an error id in a
     * HashTable if an error occurred.
     */
    List<Repository> getSpecificationRepositoriesForSystemUnderTest(SystemUnderTest sut) throws LivingDocServerException;

    /**
     * Retrieves the Requirement repository list for the project associated with
     * the specified repository or an error id in a HashTable if an error
     * occurred.
     *
     * @param repositoryUid The repository identifier to retrieve the list of
     * requirement
     * @return the Requirement repository list for the project associated with
     * the specified repository or an error id in a HashTable if an error
     * occurred.
     */
    List<Repository> getRequirementRepositoriesOfAssociatedProject(String repositoryUid) throws LivingDocServerException;

    /**
     * Retrieves the SystemUnderTest list for the project associated with the
     * specified repository or an error id in a HashTable if an error occurred.
     *
     * @param repositoryUid The repository identifier to retrieve the list of
     * sut
     * @return the SystemUnderTest list for the project associated with the
     * specified repository or an error id in a HashTable if an error occurred.
     */
    List<SystemUnderTest> getSystemUnderTestsOfAssociatedProject(String repositoryUid) throws LivingDocServerException;

    /**
     * Retrieves the SystemUnderTest list for the project associated or an error
     * id in a HashTable if an error occurred.
     *
     * @param projectName The name of the project to retrieve the list of sut
     * @return the SystemUnderTest list for the project associated or an error
     * id in a HashTable if an error occurred.
     */
    List<SystemUnderTest> getSystemUnderTestsOfProject(String projectName) throws LivingDocServerException;

    /**
     * Adds the SystemUnderTest to the SystemUnderTest list of the
     * Specification.
     *
     * @param systemUnderTest
     * @param specification
     */
    void addSpecificationSystemUnderTest(SystemUnderTest systemUnderTest, Specification specification)
        throws LivingDocServerException;

    /**
     * Removes the SystemUnderTest to the SystemUnderTest list of the
     * Specification.
     *
     * @param systemUnderTest
     * @param specification
     */
    void removeSpecificationSystemUnderTest(SystemUnderTest systemUnderTest, Specification specification)
        throws LivingDocServerException;

    /**
     * Checks if the Specification is in at least one reference.
     *
     * @param specification
     * @return true if the Specification is in at least one reference.
     */
    boolean doesSpecificationHasReferences(Specification specification) throws LivingDocServerException;

    /**
     * Retrieves the references list of the specified Specification
     *
     * @param specification
     * @return the references list of the specified Specification
     */
    List<Reference> getSpecificationReferences(Specification specification) throws LivingDocServerException;

    /**
     * Retrieve executions list of the specified Specification
     *
     * @param specification
     * @param sut
     * @param maxResults
     * @return the executions list of the specified Specification containing at
     * most max-results items
     */
    List<Execution> getSpecificationExecutions(Specification specification, SystemUnderTest sut, int maxResults)
        throws LivingDocServerException;

    /**
     * Retrieve execution for the given id.
     *
     * @param id
     * @return execution
     * @throws LivingDocServerException
     */
    Execution getSpecificationExecution(Long id) throws LivingDocServerException;

    /**
     * Checks if the Requirement is in at least one Reference.
     *
     * @param requirement
     * @return true if the Requirement is in at least one Reference.
     */
    boolean doesRequirementHasReferences(Requirement requirement) throws LivingDocServerException;

    /**
     * Retrieves the References list of the specified requirement
     *
     * @param requirement
     * @return the References list of the specified requirement
     */
    List<Reference> getRequirementReferences(Requirement requirement) throws LivingDocServerException;

    /**
     * Retrieves the Requirement summary.
     *
     * @param requirement
     * @return the Requirement summary.
     */
    RequirementSummary getRequirementSummary(Requirement requirement) throws LivingDocServerException;

    /**
     * Retrieves the Reference.
     *
     * @param reference
     * @return the Reference.
     */
    Reference getReference(Reference reference) throws LivingDocServerException;

    /**
     * Retrieves the systemUnderTest
     *
     * @param systemUnderTest
     * @param repository
     * @return the System under test
     */
    SystemUnderTest getSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository)
        throws LivingDocServerException;

    /**
     * Creates the systemUnderTest
     *
     * @param systemUnderTest
     * @param repository
     */
    void createSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository) throws LivingDocServerException;

    /**
     * Updates the systemUnderTest
     *
     * @param oldSystemUnderTestName
     * @param systemUnderTest
     * @param repository
     */
    void updateSystemUnderTest(String oldSystemUnderTestName, SystemUnderTest systemUnderTest, Repository repository)
        throws LivingDocServerException;

    /**
     * Removes the systemUnderTest
     *
     * @param systemUnderTest
     * @param repository
     */
    void removeSystemUnderTest(SystemUnderTest systemUnderTest, Repository repository) throws LivingDocServerException;

    /**
     * Sets the systemUnderTest as the project default SystemUnderTest
     *
     * @param systemUnderTest
     * @param repository
     */
    void setSystemUnderTestAsDefault(SystemUnderTest systemUnderTest, Repository repository) throws LivingDocServerException;

    /**
     * Removes the Requirement.
     *
     * @param requirement
     */
    void removeRequirement(Requirement requirement) throws LivingDocServerException;

    /**
     * Retrieves the Specification
     *
     * @param specification
     * @return the Specification
     */
    Specification getSpecification(Specification specification) throws LivingDocServerException;

    /**
     * Retrieves the Specification using the given id.
     *
     * @param id Specification id to retrieve
     * @return the specification
     * @throws LivingDocServerException
     */
    Specification getSpecificationById(Long id) throws LivingDocServerException;

    /**
     * Retrieves all Specifications for a given SystemUnderTest and Repository
     *
     * @param systemUnderTest
     * @param repository
     * @return all Specifications for a given SystemUnderTest and Repository
     */
    List<Specification> getSpecifications(SystemUnderTest systemUnderTest, Repository repository)
        throws LivingDocServerException;

    /**
     * Retrieves the Specification location list for a given SystemUnderTest and
     * Repository
     *
     * @param repositoryUID
     * @param systemUnderTestName
     * @return the Specification location list for a given SystemUnderTest and
     * Repository
     */
    List<SpecificationLocation> getListOfSpecificationLocations(String repositoryUID, String systemUnderTestName)
        throws LivingDocServerException;

    /**
     * Retrieve the spcifications hierarchy for a Repository.
     *
     * @param repository
     * @param systemUnderTest
     * @return the TestCase executed
     */
    DocumentNode getSpecificationHierarchy(Repository repository, SystemUnderTest systemUnderTest)
        throws LivingDocServerException;

    /**
     * Creates the Specification
     *
     * @param specification
     * @return the new Specification
     */
    Specification createSpecification(Specification specification) throws LivingDocServerException;

    /**
     * Updates the Specification.
     *
     * @param oldSpecification
     * @param newSpecification
     */
    void updateSpecification(Specification oldSpecification, Specification newSpecification) throws LivingDocServerException;

    /**
     * Removes the Specification.
     *
     * @param specification
     */
    void removeSpecification(Specification specification) throws LivingDocServerException;

    /**
     * Creates a Reference
     *
     * @param reference
     */
    void createReference(Reference reference) throws LivingDocServerException;

    /**
     * Update the Reference. The Old one will be deleted based on the
     * oldReferenceParams and a new One will be created based on the
     * newReferenceParams.
     *
     * @param oldReference
     * @param newReference
     * @return the updated Reference
     */
    Reference updateReference(Reference oldReference, Reference newReference) throws LivingDocServerException;

    /**
     * Deletes the specified Reference.
     *
     * @param reference
     */
    void removeReference(Reference reference) throws LivingDocServerException;

    /**
     * Creates an Execution.
     *
     * @param systemUnderTest
     * @param specification
     * @param xmlReport
     * @return the new created Execution
     * @throws LivingDocServerException
     */
    Execution createExecution(SystemUnderTest systemUnderTest, Specification specification, XmlReport xmlReport)
        throws LivingDocServerException;

    /**
     * Executes the Specification over the selected SystemUnderTest.
     *
     * @param systemUnderTest
     * @param specification
     * @param implementedVersion
     * @param locale
     * @return the Execution of the Specification over the selected
     * SystemUnderTest.
     */
    Execution runSpecification(SystemUnderTest systemUnderTest, Specification specification, boolean implementedVersion,
        String locale) throws LivingDocServerException;

    /**
     * Executes the Reference.
     *
     * @param reference
     * @param locale
     * @return the Reference executed
     */
    Reference runReference(Reference reference, String locale) throws LivingDocServerException;

    /**
     * Removes an existing Project.
     *
     * @param project
     * @param cascade Indicates to remove the project in cascading mode (remove
     * any associations)
     * @throws LivingDocServerException
     */
    void removeProject(Project project, boolean cascade) throws LivingDocServerException;
}
