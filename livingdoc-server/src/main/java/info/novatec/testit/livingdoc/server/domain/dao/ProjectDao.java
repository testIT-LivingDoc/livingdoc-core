package info.novatec.testit.livingdoc.server.domain.dao;

import java.util.List;

import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.domain.Project;


public interface ProjectDao {
    /**
     * Retrieves the Project for the specified name. If none found an
     * LivingDocServerException is thrown.
     * </p>
     * 
     * @param name of the project
     * @return the Project for the specified name.
     */
    Project getByName(String name);

    /**
     * Retrieves all the registered Projects.
     * </p>
     * 
     * @return all the registered Projects.
     */
    List<Project> getAll();

    /**
     * Saves the specified Project.
     * </p>
     * 
     * @param name of the project
     * @return the new Project.
     */
    Project create(String name) throws LivingDocServerException;

    /**
     * Removes the specified Project.
     * </p>
     * 
     * @param name of the project
     * @throws LivingDocServerException Exception
     */
    void remove(String name) throws LivingDocServerException;

    /**
     * Updates the project.
     * 
     * @param oldProjectName Name of the project to be updated
     * @param project Project information to update
     * @return newly updated project instance
     * @throws LivingDocServerException Exception
     */
    Project update(String oldProjectName, Project project) throws LivingDocServerException;
}
