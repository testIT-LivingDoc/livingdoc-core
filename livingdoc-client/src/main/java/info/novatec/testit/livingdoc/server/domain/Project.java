package info.novatec.testit.livingdoc.server.domain;

import info.novatec.testit.livingdoc.server.*;
import org.codehaus.jackson.annotate.*;

import javax.persistence.*;
import java.util.*;

import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.*;


/**
 * Project Class. Definition of a project.
 * <p>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 */

@Entity
@Table(name = "PROJECT")
@SuppressWarnings("serial")
public class Project extends AbstractVersionedEntity implements Comparable<Project> {
    private String name;
    private Set<Repository> repositories = new HashSet<Repository>();
    private Set<SystemUnderTest> systemUnderTests = new HashSet<SystemUnderTest>();

    public static Project newInstance(String name) {
        Project project = new Project();
        project.setName(name);
        return project;
    }

    @Basic
    @Column(name = "NAME", unique = true, nullable = false, length = 255)
    public String getName() {
        return name;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<Repository> getRepositories() {
        return repositories;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    public Set<SystemUnderTest> getSystemUnderTests() {
        return systemUnderTests;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRepositories(Set<Repository> repositories) {
        this.repositories = repositories;
    }

    public void setSystemUnderTests(Set<SystemUnderTest> systemUnderTests) {
        this.systemUnderTests = systemUnderTests;
    }

    public void addRepository(Repository repo) throws LivingDocServerException {
        if (findRepositoryByName(repo.getName()) != null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.REPOSITORY_ALREADY_EXISTS,
                    "Repository already exists");
        }

        repo.setProject(this);
        repositories.add(repo);
    }

    public void removeRepository(Repository repo) throws LivingDocServerException {
        if (!repositories.contains(repo)) {
            throw new LivingDocServerException(LivingDocServerErrorKey.REPOSITORY_NOT_FOUND, "Repository not found");
        }

        repositories.remove(repo);
        repo.setProject(null);
    }

    public void addSystemUnderTest(SystemUnderTest sut) throws LivingDocServerException {
        if (findSystemUnderTestByName(sut.getName()) != null) {
            throw new LivingDocServerException(LivingDocServerErrorKey.SUT_ALREADY_EXISTS, "Sut name already exists");
        }

        if (systemUnderTests.isEmpty()) {
            sut.setIsDefault(true);
        }
        systemUnderTests.add(sut);
        sut.setProject(this);
    }

    public void removeSystemUnderTest(SystemUnderTest sut) throws LivingDocServerException {
        if (!systemUnderTests.contains(sut)) {
            throw new LivingDocServerException(LivingDocServerErrorKey.SUT_NOT_FOUND, "Sut not found");
        }

        systemUnderTests.remove(sut);
        if (sut.isDefault() && !systemUnderTests.isEmpty()) {
            systemUnderTests.iterator().next().setIsDefault(true);
        }

        sut.setProject(null);
    }

    @JsonIgnore
    @Transient
    public SystemUnderTest getDefaultSystemUnderTest() {
        for (SystemUnderTest sut : systemUnderTests) {
            if (sut.isDefault()) {
                return sut;
            }
        }

        return null;
    }

    @Override
    public Vector<Object> marshallize() {
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(PROJECT_NAME_IDX, name);
        return parameters;
    }

    public Project marshallizeRest() {
        Project returnValue = Project.newInstance(this.getName());
        returnValue.setId(this.getId());
        returnValue.setVersion(this.getVersion());
        return returnValue;
    }

    @Override
    public int compareTo(Project o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Project)) {
            return false;
        }

        Project projectCompared = (Project) o;

        return getName().equals(projectCompared.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    private Repository findRepositoryByName(String repoName) {
        for (Repository repository : repositories) {
            if (repository.getName().equalsIgnoreCase(repoName)) {
                return repository;
            }
        }
        return null;
    }

    private SystemUnderTest findSystemUnderTestByName(String sutName) {
        for (SystemUnderTest sut : systemUnderTests) {
            if (sut.getName().equalsIgnoreCase(sutName)) {
                return sut;
            }
        }
        return null;
    }
}
