package info.novatec.testit.livingdoc.server.domain.dao;

import java.util.List;

import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.domain.Repository;
import info.novatec.testit.livingdoc.server.domain.RepositoryType;
import info.novatec.testit.livingdoc.server.domain.component.ContentType;


public interface RepositoryDao {
    /**
     * Retrieves the Repository. If none found an LivingDocServerException is
     * thrown.
     * </p>
     * 
     * @param repositoryUid
     * @return the Repository.
     */
    Repository getByUID(String repositoryUID);

    /**
     * Retrieves the Repository. If none found an LivingDocServerException is
     * thrown.
     * </p>
     * 
     * @param projectName
     * @param repositoryName
     * @return the Repository.
     */
    Repository getByName(String projectName, String repositoryName);

    /**
     * Retrieves all the registered Repositories.
     * </p>
     * 
     * @return the repositories
     */
    List<Repository> getAll();

    /**
     * Retrieves all the registered Repositories for a project.
     * </p>
     * 
     * @param projectName
     * @return all the registered Repositories for a project.
     */
    List<Repository> getAll(String projectName);

    /**
     * Retrieves all the registered Test Repositories for project.
     * </p>
     * 
     * @param projectName
     * @return the Tests repositories
     */
    List<Repository> getAllTestRepositories(String projectName);

    /**
     * Retrieves all the registered Requirement Repositories for a project.
     * </p>
     * 
     * @param projectName
     * @return the Requirements repositories
     */

    List<Repository> getAllRequirementRepositories(String projectName);

    /**
     * Retrieve all the repository of a certain type.
     * 
     * @param contentType
     * @return
     */
    List<Repository> getAllRepositories(ContentType contentType);

    /**
     * Retrieves the repository type by name.
     * </p>
     * 
     * @param repositoryTypeName
     * @return the repository type.
     */
    RepositoryType getTypeByName(String repositoryTypeName);

    /**
     * Creates a new Repository.
     * </p>
     * 
     * @param newRepository
     * @return the created repository
     * @throws LivingDocServerException
     */
    Repository create(Repository newRepository) throws LivingDocServerException;

    /**
     * Updates the Repository.
     * </p>
     * 
     * @param repository
     * @throws LivingDocServerException
     */
    void update(Repository repository) throws LivingDocServerException;

    /**
     * Removes the repository if this one doesnt hold any specifications
     * 
     * @param repositoryUid
     * @throws LivingDocServerException
     */
    void remove(String repositoryUid) throws LivingDocServerException;

    /**
     * Retrieves all available RepositoryTypes.
     * <p>
     * 
     * @return
     */
    List<RepositoryType> getAllTypes();

    /**
     * Create a new Repository Type
     * </p>
     * 
     * @param Repository type
     * @return the Requirement type created
     */
    RepositoryType create(RepositoryType repositoryType);

}
