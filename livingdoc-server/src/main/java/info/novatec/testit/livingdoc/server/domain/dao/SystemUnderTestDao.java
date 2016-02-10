package info.novatec.testit.livingdoc.server.domain.dao;

import java.util.List;

import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.domain.Reference;
import info.novatec.testit.livingdoc.server.domain.Runner;
import info.novatec.testit.livingdoc.server.domain.Specification;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;


public interface SystemUnderTestDao {

    /**
     * Retrieves the Runner for the specified name.
     * </p>
     * 
     * @param name of the runner
     * @return the Runner for the specified name.
     */
    Runner getRunnerByName(String name);

    /**
     * Retrieves All available runners.
     * </p>
     * 
     * @return All available runners.
     */
    List<Runner> getAllRunners();

    /**
     * Creates the Runner
     * </p>
     * 
     * @param runner
     * @return the new runner.
     * @throws LivingDocServerException
     */
    Runner create(Runner runner) throws LivingDocServerException;

    /**
     * Updates the runner.
     * </p>
     * 
     * @param oldRunnerName
     * @param runner
     * @return the updated runner.
     * @throws LivingDocServerException
     */
    Runner update(String oldRunnerName, Runner runner) throws LivingDocServerException;

    /**
     * Removes the runner.
     * </p>
     * 
     * @param runnerName
     * @throws LivingDocServerException
     */
    void removeRunner(String runnerName) throws LivingDocServerException;

    /**
     * Retrieves the SystemUnderTest for the specified name.
     * </p>
     * 
     * @param name of the project
     * @param name of the SUT
     * @return the SystemUnderTest for the specified name.
     */
    SystemUnderTest getByName(String projectName, String sutName);

    /**
     * Retrieves all the SystemUnderTest for the registered Project.
     * </p>
     * 
     * @param projectName
     * @return all the SystemUnderTest for the registered Project.
     */
    List<SystemUnderTest> getAllForProject(String projectName);

    /**
     * Retrieves all the SystemUnderTest for the registered Runner.
     * </p>
     * 
     * @param runnerName
     * @return all the SystemUnderTest for the registered Runner.
     */
    List<SystemUnderTest> getAllForRunner(String runnerName);

    /**
     * Saves the specified SystemUnderTest.
     * </p>
     * 
     * @param newSystemUnderTest
     * @return the new SystemUnderTest.
     * @throws LivingDocServerException
     */
    SystemUnderTest create(SystemUnderTest newSystemUnderTest) throws LivingDocServerException;

    /**
     * Updates the specified SystemUnderTest.
     * </p>
     * 
     * @param oldSutName
     * @param updatedSystemUnderTest
     * @return the updated SystemUnderTest.
     * @throws LivingDocServerException
     */
    SystemUnderTest update(String oldSutName, SystemUnderTest updatedSystemUnderTest) throws LivingDocServerException;

    /**
     * Deletes the specified SystemUnderTest.
     * </p>
     * 
     * @param projectName.
     * @param sutName.
     * @throws LivingDocServerException
     */
    void remove(String projectName, String sutName) throws LivingDocServerException;

    /**
     * Set the specified SystemUnderTest as the new project default.
     * </p>
     * 
     * @param systemUnderTest.
     * @throws LivingDocServerException
     */
    void setAsDefault(SystemUnderTest systemUnderTest) throws LivingDocServerException;

    /**
     * Retrieves all references that depends on the SystemUnderTest
     * </p>
     * 
     * @param sut
     * @return all references that depends on the SystemUnderTest
     */
    List<Reference> getAllReferences(SystemUnderTest sut);

    /**
     * Retrieves all specifications that depends on the SystemUnderTest
     * </p>
     * 
     * @param sut
     * @return all specifications that depends on the SystemUnderTest
     */
    List<Specification> getAllSpecifications(SystemUnderTest sut);
}
