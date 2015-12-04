package info.novatec.testit.livingdoc.server.domain.dao;

import java.util.List;

import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.domain.Execution;
import info.novatec.testit.livingdoc.server.domain.Reference;
import info.novatec.testit.livingdoc.server.domain.Repository;
import info.novatec.testit.livingdoc.server.domain.Requirement;
import info.novatec.testit.livingdoc.server.domain.Specification;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;


public interface DocumentDao {
    /**
     * Retrieves the Requirement for the specified repository UID. If none found
     * an LivingDocServerException is thrown.
     * </p>
     * 
     * @param repositoryUid
     * @param requirementName
     * @return the Requirement for the specified repository UID.
     */
    public Requirement getRequirementByName(String repositoryUid, String requirementName);

    /**
     * Saves the Requirement for the specified repository UID.
     * </p>
     * 
     * @param repositoryUid
     * @param requirementName
     * @return the new Requirement
     * @throws LivingDocServerException
     */
    public Requirement createRequirement(String repositoryUid, String requirementName) throws LivingDocServerException;

    /**
     * Retrieves the Requirement for the specified repository UID. If none found
     * then a new is saved and returned.
     * </p>
     * 
     * @param repositoryUid
     * @param requirementName
     * @return the retrieved/created Requirement
     * @throws LivingDocServerException
     */
    public Requirement getOrCreateRequirement(String repositoryUid, String requirementName) throws LivingDocServerException;

    /**
     * Removes the Requirement.
     * </p>
     * 
     * @param requirement
     * @throws LivingDocServerException
     */
    public void removeRequirement(Requirement requirement) throws LivingDocServerException;

    /**
     * Retrieves the Specification for the specified repository UID.
     * </p>
     * 
     * @param repositoryUid
     * @param specificationName
     * @return the Specification for the specified repository UID.
     */
    public Specification getSpecificationByName(String repositoryUid, String specificationName);

    /**
     * Retrieves the Specification for the specified id.
     * </p>
     * 
     * @param id Specification id to retrieve
     * @return the Specification for the given id
     */
    public Specification getSpecificationById(Long id);

    /**
     * Saves the Specification for the specified repository UID.
     * </p>
     * 
     * @param systemUnderTestName
     * @param repositoryUid
     * @param specificationName
     * @return the new Specification
     * @throws LivingDocServerException
     */
    public Specification createSpecification(String systemUnderTestName, String repositoryUid, String specificationName)
        throws LivingDocServerException;

    /**
     * Retrieves the Specification for the specified repository UID. If none
     * found then a new is saved and returned.
     * </p>
     * 
     * @param systemUnderTestName
     * @param repositoryUid
     * @param specificationName
     * @return the retrieved/created Specification
     * @throws LivingDocServerException
     */
    public Specification getOrCreateSpecification(String systemUnderTestName, String repositoryUid, String specificationName)
        throws LivingDocServerException;

    /**
     * UPdates the Specification.
     * </p>
     * 
     * @param newSpecification
     * @param Specification
     * @throws LivingDocServerException
     */
    public void updateSpecification(Specification oldSpecification, Specification newSpecification)
        throws LivingDocServerException;

    /**
     * Removes the Specification.
     * </p>
     * 
     * @param Specification
     * @throws LivingDocServerException
     */
    public void removeSpecification(Specification specification) throws LivingDocServerException;

    /**
     * Retrieves the Reference from dataBase.
     * </p>
     * 
     * @param Reference
     * @return the Reference from dataBase.
     */
    public Reference get(Reference reference);

    /**
     * Retrieves the list of References linked to the Specification
     * </p>
     * 
     * @param Specification
     * @return the list of References linked to the Specification
     */
    public List<Reference> getAllReferences(Specification specification);

    /**
     * Retrieves the list of References linked to the Requirement
     * </p>
     * 
     * @param Requirement
     * @return the list of References linked to the Requirement
     */
    public List<Reference> getAllReferences(Requirement requirement);

    /**
     * Adds the SystemUnderTest to the SystemUnderTest list of the Specification
     * </p>
     * 
     * @param systemUnderTest
     * @param specification
     * @throws LivingDocServerException
     */
    public void addSystemUnderTest(SystemUnderTest systemUnderTest, Specification specification)
        throws LivingDocServerException;

    /**
     * Removes the SystemUnderTest to the SystemUnderTest list of the
     * Specification
     * </p>
     * 
     * @param systemUnderTest
     * @param specification
     * @throws LivingDocServerException
     */
    public void removeSystemUnderTest(SystemUnderTest systemUnderTest, Specification specification)
        throws LivingDocServerException;

    /**
     * Creates the Reference. The Project, the repositories and the System under
     * test have to exist else an exception will be thrown.
     * </p>
     * 
     * @param Reference
     * @return the new Created Reference
     * @throws LivingDocServerException
     */
    public Reference createReference(Reference reference) throws LivingDocServerException;

    /**
     * Deletes the Reference
     * </p>
     * 
     * @param Reference
     * @throws LivingDocServerException
     */
    public void removeReference(Reference reference) throws LivingDocServerException;

    /**
     * Updates the old Reference with the new one. Basically removes the old one
     * and creates a new one.
     * </p>
     * 
     * @param oldReference
     * @param newReference
     * @return the updated Reference
     * @throws LivingDocServerException
     */
    public Reference updateReference(Reference oldReference, Reference newReference) throws LivingDocServerException;

    /**
     * Creates the Execution.
     * 
     * @param execution
     * @return the new created Execution
     * @throws LivingDocServerException
     */
    public Execution createExecution(Execution execution) throws LivingDocServerException;

    /**
     * Run the Specification on the SystemUnderTest.
     * </p>
     * 
     * @param systemUnderTest
     * @param specification
     * @param implemeted
     * @param locale
     * @return the execution of the Specification on the SystemUnderTest.
     * @throws LivingDocServerException
     */
    public Execution runSpecification(SystemUnderTest systemUnderTest, Specification specification, boolean implemeted,
        String locale) throws LivingDocServerException;

    /**
     * Run the Specification of the reference.
     * </p>
     * 
     * @param reference
     * @param locale
     * @return the executed Reference
     * @throws LivingDocServerException
     */
    public Reference runReference(Reference reference, String locale) throws LivingDocServerException;

    /**
     * Retrieves all Specifications for a given SystemUnderTest and Repository
     * <p>
     * 
     * @param sut
     * @param repository
     * @return all Specifications for a given SystemUnderTest and Repository
     */
    public List<Specification> getSpecifications(SystemUnderTest sut, Repository repository);

    /**
     * Retrieve specification Executions for a given Specification where the
     * specification has been executed before the given start date.
     * 
     * @param specification
     * @param sut
     * @param maxResults
     * @return Specification executions containing at most the max-result items
     */
    public List<Execution> getSpecificationExecutions(Specification specification, SystemUnderTest sut, int maxResults);

    /**
     * Retrieve an Execution for the given id.
     * 
     * @param id
     * @return execution for the given id
     * @throws LivingDocServerException
     */
    public Execution getSpecificationExecution(Long id);
}
