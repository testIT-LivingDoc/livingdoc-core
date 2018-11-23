package info.novatec.testit.livingdoc.server.domain;

import info.novatec.testit.livingdoc.repository.DocumentRepository;
import info.novatec.testit.livingdoc.server.LivingDocServerErrorKey;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.domain.component.ContentType;
import info.novatec.testit.livingdoc.util.ClassUtils;
import info.novatec.testit.livingdoc.util.EncryptionUtil;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.*;


/**
 * Repository Class. Definition of the repository.
 * <p/>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 */

@Entity
@Table(name = "REPOSITORY", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME", "PROJECT_ID" }) })
@SuppressWarnings("serial")
public class Repository extends AbstractVersionedEntity implements Comparable<Repository> {
    private String name;
    private String uid;
    private String baseUrl;
    private String repositoryBaseUrl;
    private String testBaseUrl;
    private ContentType contentType = ContentType.UNKNOWN;
    private String username;
    private String password;

    private Project project;
    private RepositoryType type;
    private Set<Requirement> requirements = new HashSet<Requirement>();
    private Set<Specification> specifications = new HashSet<Specification>();
    private int maxUsers;

    public static Repository newInstance(String uid) {
        Repository repository = new Repository();
        repository.setUid(uid);
        return repository;
    }

    @Basic
    @Column(name = "NAME", nullable = false, length = 255)
    public String getName() {
        return this.name;
    }

    @Basic
    @Column(name = "UIDENT", unique = true, nullable = false, length = 255)
    public String getUid() {
        return this.uid;
    }

    @Basic
    @Column(name = "BASE_URL", nullable = false, length = 255)
    public String getBaseUrl() {
        return this.baseUrl;
    }

    @Basic
    @Column(name = "BASE_REPOSITORY_URL", nullable = false, length = 255)
    public String getBaseRepositoryUrl() {
        return this.repositoryBaseUrl;
    }

    @Basic
    @Column(name = "BASE_TEST_URL", nullable = false, length = 255)
    public String getBaseTestUrl() {
        return this.testBaseUrl;
    }

    @Basic
    @Column(name = "USERNAME", nullable = true)
    public String getUsername() {
        return username;
    }

    @Basic
    @Column(name = "PASSWORD", nullable = true)
    public String getPassword() {
        return password;
    }

    @Transient
    public String getDecryptedPassword() {
        return new EncryptionUtil().decrypt(password);}

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "PROJECT_ID")
    public Project getProject() {
        return project;
    }

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "REPOSITORY_TYPE_ID")
    public RepositoryType getType() {
        return this.type;
    }

    @OneToMany(mappedBy = "repository", cascade = CascadeType.ALL)
    public Set<Requirement> getRequirements() {
        return this.requirements;
    }

    @OneToMany(mappedBy = "repository", cascade = CascadeType.ALL)
    public Set<Specification> getSpecifications() {
        return specifications;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setBaseRepositoryUrl(String repositoryBaseUrl) {
        this.repositoryBaseUrl = repositoryBaseUrl;
    }

    public void setBaseTestUrl(String testBaseUrl) {
        this.testBaseUrl = testBaseUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = new EncryptionUtil().encrypt(password);
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setType(RepositoryType type) {
        this.type = type;
    }

    public void setRequirements(Set<Requirement> requirements) {
        this.requirements = requirements;
    }

    public void setSpecifications(Set<Specification> specifications) {
        this.specifications = specifications;
    }

    public void addRequirement(Requirement requirement) throws LivingDocServerException {
        if (requirements.contains(requirement) || requirementNameExists(requirement.getName())) {
            throw new LivingDocServerException(LivingDocServerErrorKey.REQUIREMENT_ALREADY_EXISTS,
                "Requirement already exists");
        }

        requirement.setRepository(this);
        requirements.add(requirement);
    }

    public void removeRequirement(Requirement requirement) throws LivingDocServerException {
        if ( ! requirements.contains(requirement)) {
            throw new LivingDocServerException(LivingDocServerErrorKey.REQUIREMENT_NOT_FOUND, "Requirement not found");
        }

        requirements.remove(requirement);
        requirement.setRepository(null);
    }

    public void addSpecification(Specification specification) throws LivingDocServerException {
        if (specifications.contains(specification) || specificationNameExists(specification.getName())) {
            throw new LivingDocServerException(LivingDocServerErrorKey.SPECIFICATION_ALREADY_EXISTS,
                "Specification already exists");
        }

        specification.setRepository(this);
        specifications.add(specification);
    }

    public void removeSpecification(Specification specification) throws LivingDocServerException {
        if ( ! specifications.contains(specification)) {
            throw new LivingDocServerException(LivingDocServerErrorKey.SPECIFICATION_NOT_FOUND, "Specification not found");
        }

        specifications.remove(specification);
        specification.setRepository(null);
    }

    @Transient
    public int getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
    }

    public String resolveName(Document document) {
        return type.resolveName(document);
    }

    public String asCmdLineOption() {
        return type.asFactoryArguments(this, false, null, null);
    }

    public DocumentRepository asDocumentRepository(ClassLoader classLoader) throws UndeclaredThrowableException {
        return asDocumentRepository(classLoader, null, null);
    }

    public DocumentRepository asDocumentRepository(ClassLoader classLoader, String user, String pwd)
        throws UndeclaredThrowableException {
        return ClassUtils.createInstanceFromClassNameWithArguments(classLoader, type.asFactoryArguments(this, true, user,
            pwd), DocumentRepository.class);
    }

    @Override
    public Vector<Object> marshallize() {
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(REPOSITORY_NAME_IDX, StringUtils.stripToEmpty(name));
        parameters.add(REPOSITORY_UID_IDX, StringUtils.stripToEmpty(uid));
        parameters.add(REPOSITORY_PROJECT_IDX, project != null ? project.marshallize() : Project.newInstance("")
            .marshallize());
        parameters.add(REPOSITORY_TYPE_IDX, type != null ? type.marshallize() : RepositoryType.newInstance("")
            .marshallize());
        parameters.add(REPOSITORY_CONTENTTYPE_IDX, contentType.toString());
        parameters.add(REPOSITORY_BASE_URL_IDX, StringUtils.stripToEmpty(getBaseUrl()));
        parameters.add(REPOSITORY_BASEREPO_URL_IDX, StringUtils.stripToEmpty(getBaseRepositoryUrl()));
        parameters.add(REPOSITORY_BASETEST_URL_IDX, StringUtils.stripToEmpty(getBaseTestUrl()));
        parameters.add(REPOSITORY_USERNAME_IDX, StringUtils.stripToEmpty(username));
        parameters.add(REPOSITORY_PASSWORD_IDX, StringUtils.stripToEmpty(getDecryptedPassword()));
        parameters.add(REPOSITORY_MAX_USERS_IDX, maxUsers);
        return parameters;
    }

    @Override
    public int compareTo(Repository other) {
        return getName().compareTo( ( other ).getName());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || ! ( o instanceof Repository )) {
            return false;
        }

        Repository repoCompared = ( Repository ) o;
        return getUid().equals(repoCompared.getUid());
    }

    @Override
    public int hashCode() {
        return getUid() == null ? 0 : getUid().hashCode();
    }

    private boolean requirementNameExists(String requirementName) {
        for (Requirement requirement : requirements) {
            if (requirement.getName().equalsIgnoreCase(requirementName)) {
                return true;
            }
        }

        return false;
    }

    private boolean specificationNameExists(String specificationName) {
        for (Specification specification : specifications) {
            if (specification.getName().equalsIgnoreCase(specificationName)) {
                return true;
            }
        }

        return false;
    }
}
