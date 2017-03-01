package info.novatec.testit.livingdoc.server.rpc.xmlrpc;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.server.LivingDocServerErrorKey;
import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.domain.ClasspathSet;
import info.novatec.testit.livingdoc.server.domain.DocumentNode;
import info.novatec.testit.livingdoc.server.domain.Execution;
import info.novatec.testit.livingdoc.server.domain.Project;
import info.novatec.testit.livingdoc.server.domain.Reference;
import info.novatec.testit.livingdoc.server.domain.ReferenceNode;
import info.novatec.testit.livingdoc.server.domain.Repository;
import info.novatec.testit.livingdoc.server.domain.RepositoryType;
import info.novatec.testit.livingdoc.server.domain.Requirement;
import info.novatec.testit.livingdoc.server.domain.RequirementSummary;
import info.novatec.testit.livingdoc.server.domain.Runner;
import info.novatec.testit.livingdoc.server.domain.Specification;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTestByNameComparator;
import info.novatec.testit.livingdoc.server.domain.component.ContentType;
import info.novatec.testit.livingdoc.server.transfer.ExecutionResult;
import info.novatec.testit.livingdoc.server.transfer.SpecificationLocation;
import info.novatec.testit.livingdoc.util.ClientUtils;
import info.novatec.testit.livingdoc.util.FormattedDate;


/**
 * The XML-RPC Data Marshaller. Provides static methods to pass from POJO to
 * XML-RPC supported objects. Provides static methods to pass from XML-RPC
 * supported objects to POJO.
 * <p>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 */
public class XmlRpcDataMarshaller {
    private static final Logger log = LoggerFactory.getLogger(XmlRpcDataMarshaller.class);
    public static final String MARSHALLING_VERSION = "Marshall v. 1.0";

    public static final int PROJECT_NAME_IDX = 0;

    public static final int REPOSITORY_TYPE_NAME_IDX = 0;
    public static final int REPOSITORY_TYPE_REPOCLASS_IDX = 1;
    public static final int REPOSITORY_TYPE_NAME_FORMAT_IDX = 2;
    public static final int REPOSITORY_TYPE_URI_FORMAT_IDX = 3;

    public static final int REPOSITORY_NAME_IDX = 0;
    public static final int REPOSITORY_UID_IDX = 1;
    public static final int REPOSITORY_PROJECT_IDX = 2;
    public static final int REPOSITORY_TYPE_IDX = 3;
    public static final int REPOSITORY_CONTENTTYPE_IDX = 4;
    public static final int REPOSITORY_BASE_URL_IDX = 5;
    public static final int REPOSITORY_BASEREPO_URL_IDX = 6;
    public static final int REPOSITORY_BASETEST_URL_IDX = 7;
    public static final int REPOSITORY_USERNAME_IDX = 8;
    public static final int REPOSITORY_PASSWORD_IDX = 9;
    public static final int REPOSITORY_MAX_USERS_IDX = 10;

    public static final int DOCUMENT_NAME_IDX = 0;
    public static final int DOCUMENT_REPOSITORY_IDX = 1;
    public static final int SPECIFICATION_SUTS_IDX = 2;

    public static final int RUNNER_NAME_IDX = 0;
    public static final int RUNNER_SERVER_NAME_IDX = 1;
    public static final int RUNNER_SERVER_PORT_IDX = 2;
    public static final int RUNNER_CLASSPATH_IDX = 3;
    public static final int RUNNER_SECURED_IDX = 4;

    public static final int ENVTYPE_NAME_IDX = 0;

    public static final int SUT_NAME_IDX = 0;
    public static final int SUT_PROJECT_IDX = 1;
    public static final int SUT_CLASSPATH_IDX = 2;
    public static final int SUT_FIXTURE_CLASSPATH_IDX = 3;
    public static final int SUT_FIXTURE_FACTORY_IDX = 4;
    public static final int SUT_FIXTURE_FACTORY_ARGS_IDX = 5;
    public static final int SUT_IS_DEFAULT_IDX = 6;
    public static final int SUT_RUNNER_IDX = 7;
    public static final int SUT_PROJECT_DEPENDENCY_DESCRIPTOR_IDX = 8;

    public static final int REFERENCE_REQUIREMENT_IDX = 0;
    public static final int REFERENCE_SPECIFICATION_IDX = 1;
    public static final int REFERENCE_SUT_IDX = 2;
    public static final int REFERENCE_SECTIONS_IDX = 3;
    public static final int REFERENCE_LAST_EXECUTION_IDX = 4;

    public static final int EXECUTION_RESULTS_IDX = 0;
    public static final int EXECUTION_ERRORID_IDX = 1;
    public static final int EXECUTION_FAILIURES_IDX = 2;
    public static final int EXECUTION_ERRORS_IDX = 3;
    public static final int EXECUTION_SUCCESS_IDX = 4;
    public static final int EXECUTION_IGNORED_IDX = 5;
    public static final int EXECUTION_EXECUTION_DATE_IDX = 6;

    public static final int SUMMARY_REFERENCES_IDX = 0;
    public static final int SUMMARY_FAILIURES_IDX = 1;
    public static final int SUMMARY_ERRORS_IDX = 2;
    public static final int SUMMARY_SUCCESS_IDX = 3;
    public static final int SUMMARY_EXCEPTION_IDX = 4;

    public final static int NODE_TITLE_INDEX = 0;
    public final static int NODE_EXECUTABLE_INDEX = 1;
    public final static int NODE_CAN_BE_IMPLEMENTED_INDEX = 2;
    public final static int NODE_CHILDREN_INDEX = 3;
    public final static int NODE_REPOSITORY_UID_INDEX = 4;
    public final static int NODE_SUT_NAME_INDEX = 5;
    public final static int NODE_SECTION_INDEX = 6;

    public static final String SUPPRESS_UNCHECKED = "unchecked";

    /**
     * Transforms the Collection of projects into a Vector of project
     * parameters.
     * <p>
     *
     * @param projects
     * @return the Collection of projects into a Vector of projects parameters
     */
    public static Vector<Object> toXmlRpcProjectsParameters(Collection<Project> projects) {
        Vector<Object> projectsParams = new Vector<Object>();
        for (Project project : projects) {
            projectsParams.add(project.marshallize());
        }

        return projectsParams;
    }

    /**
     * Transforms the Collection of runners into a Vector of runners parameters.
     * <p>
     *
     * @param runners
     * @return the Collection of runners into a Vector of runners parameters
     */
    public static Vector<Object> toXmlRpcRunnersParameters(Collection<Runner> runners) {
        Vector<Object> runnersParams = new Vector<Object>();
        for (Runner runner : runners) {
            runnersParams.add(runner.marshallize());
        }

        return runnersParams;
    }

    /**
     * Transforms the Collection of SystemUnderTests into a Vector of
     * SystemUnderTests parameters.
     * <p>
     *
     * @param suts
     * @return the Collection of SystemUnderTests into a Vector of
     * SystemUnderTests parameters
     */
    public static Vector<Object> toXmlRpcSystemUnderTestsParameters(Collection<SystemUnderTest> suts) {
        Vector<Object> sutsParams = new Vector<Object>();
        for (SystemUnderTest sut : suts) {
            sutsParams.add(sut.marshallize());
        }

        return sutsParams;
    }

    /**
     * Transforms the Collection of Repositories into a Vector of Repositories
     * parameters by repository types.
     * <p>
     *
     * @param repositories
     * @return the Collection of Repositories into a Vector of Repositories
     * parameters by type.
     */
    public static Vector<Object> toXmlRpcRepositoriesParameters(Collection<Repository> repositories) {
        Vector<Object> repositoriesParams = new Vector<Object>();
        for (Repository repo : repositories) {
            repositoriesParams.add(repo.marshallize());
        }

        return repositoriesParams;
    }

    /**
     * Transforms the Collection of Specifications into a Vector of
     * Specification parameters.
     * <p>
     *
     * @param specifications
     * @return the Collection of Specifications into a Vector of Specification
     * parameters
     */
    public static Vector<Object> toXmlRpcSpecificationsParameters(Collection<Specification> specifications) {
        Vector<Object> specificationsParams = new Vector<Object>();
        for (Specification specification : specifications) {
            specificationsParams.add(specification.marshallize());
        }

        return specificationsParams;
    }

    /**
     * Transforms the Collection of Specification locations into a Vector of
     * Specification location parameters.
     * <p>
     *
     * @param specificationLocations
     * @return the Collection of Specifications into a Vector of Specification
     * location parameters
     */
    public static Vector<Object> toXmlRpcSpecificationLocationsParameters(
        Collection<SpecificationLocation> specificationLocations) {
        Vector<Object> specificationLocationsParams = new Vector<Object>();
        for (SpecificationLocation specificationLoc : specificationLocations) {
            specificationLocationsParams.add(specificationLoc.marshallize());
        }
        log.debug(ToStringBuilder.reflectionToString(specificationLocationsParams));
        return specificationLocationsParams;
    }

    /**
     * Transforms the Collection of References into a Vector of Reference
     * parameters.
     * <p>
     *
     * @param references
     * @return the Collection of References into a Vector of Reference
     * parameters
     */
    public static Vector<Object> toXmlRpcReferencesParameters(Collection<Reference> references) {
        Vector<Object> referencesParams = new Vector<Object>();
        for (Reference reference : references) {
            referencesParams.add(reference.marshallize());
        }

        return referencesParams;
    }

    /**
     * Transforms the Vector of the Execution result parameters into a
     * {@link ExecutionResult} Object.
     *
     * @param xmlRpcParameters
     * @return the execution result.
     */
    public static ExecutionResult toExecutionResult(Vector<Object> xmlRpcParameters) {
        ExecutionResult executionResult = null;
        if ( ! xmlRpcParameters.isEmpty()) {
            executionResult = new ExecutionResult();
            executionResult.setSpaceKey(( String ) xmlRpcParameters.get(ExecutionResult.SPACEKEY_IDX));
            executionResult.setPageTitle(( String ) xmlRpcParameters.get(ExecutionResult.PAGETITLE_IDX));
            executionResult.setSut(( String ) xmlRpcParameters.get(ExecutionResult.SUT_IDX));
            executionResult.setXmlReport(( String ) xmlRpcParameters.get(ExecutionResult.XMLREPORT_IDX));
        }
        return executionResult;
    }

    /**
     * Transforms the Vector of the specification location parameters into a
     * {@link SpecificationLocation} Object.
     *
     * @param xmlRpcParameters
     * @return the specification location.
     */
    public static SpecificationLocation toSpecificationLocation(Vector<String> xmlRpcParameters) {
        SpecificationLocation specLoc = null;
        if ( ! xmlRpcParameters.isEmpty()) {
            specLoc = new SpecificationLocation();
            specLoc.setRepositoryTypeClassName(xmlRpcParameters.get(SpecificationLocation.REPOSITORY_TYPE_CLASSNAME_IDX));
            specLoc.setBaseTestUrl(xmlRpcParameters.get(SpecificationLocation.BASE_TEST_URL_IDX));
            specLoc.setUsername(xmlRpcParameters.get(SpecificationLocation.USERNAME_IDX));
            specLoc.setPassword(xmlRpcParameters.get(SpecificationLocation.PASSWORD_IDX));
            specLoc.setSpecificationName(xmlRpcParameters.get(SpecificationLocation.SPEC_NAME_IDX));
        }
        return specLoc;
    }

    /**
     * Transforms the Vector of the Project parameters into a {@link Project}
     * Object. <br>
     * Structure of the parameters:<br>
     * Vector[name]
     * <p>
     *
     * @param xmlRpcParameters
     * @return the Project.
     */
    public static Project toProject(Vector<Object> xmlRpcParameters) {
        Project project = null;
        if ( ! xmlRpcParameters.isEmpty()) {
            project = Project.newInstance(( String ) xmlRpcParameters.get(PROJECT_NAME_IDX));
        }

        return project;
    }

    /**
     * Transforms the Vector of the RepositoryType parameters into a
     * RepositoryType Object.<br>
     * Structure of the parameters:<br>
     * Vector[name, uriFormat]
     * <p>
     *
     * @param xmlRpcParameters
     * @return the RepositoryType.
     */
    public static RepositoryType toRepositoryType(Vector<Object> xmlRpcParameters) {
        RepositoryType repositoryType = null;
        if ( ! xmlRpcParameters.isEmpty()) {
            log.debug("Extracting repository type from XML-RPC parameters %s", xmlRpcParameters);
            repositoryType = RepositoryType.newInstance(( String ) xmlRpcParameters.get(REPOSITORY_TYPE_NAME_IDX));
            String repositoryClass = ( String ) xmlRpcParameters.get(REPOSITORY_TYPE_REPOCLASS_IDX);
            repositoryType.setClassName(repositoryClass);
            repositoryType.setDocumentUrlFormat(StringUtils.stripToNull(( String ) xmlRpcParameters.get(
                REPOSITORY_TYPE_NAME_FORMAT_IDX)));
            repositoryType.setTestUrlFormat(StringUtils.stripToNull(( String ) xmlRpcParameters.get(
                REPOSITORY_TYPE_URI_FORMAT_IDX)));
        }

        return repositoryType;
    }

    /**
     * Transforms the Vector of the Repository parameters into a Repository
     * Object.<br>
     * Structure of the parameters:<br>
     * Vector[name, Vector[project parameters], type, content type, uri]
     * <p>
     *
     * @param xmlRpcParameters
     * @return the Repository.
     */
    @SuppressWarnings(SUPPRESS_UNCHECKED)
    public static Repository toRepository(Vector<Object> xmlRpcParameters) {
        Repository repository = null;
        if ( ! xmlRpcParameters.isEmpty()) {
            log.debug("Extracting repository from XML-RPC parameters %s", xmlRpcParameters);

            repository = Repository.newInstance(( String ) xmlRpcParameters.get(REPOSITORY_UID_IDX));
            repository.setProject(toProject(( Vector<Object> ) xmlRpcParameters.get(REPOSITORY_PROJECT_IDX)));
            repository.setType(toRepositoryType(( Vector<Object> ) xmlRpcParameters.get(REPOSITORY_TYPE_IDX)));
            repository.setName(( String ) xmlRpcParameters.get(REPOSITORY_NAME_IDX));
            repository.setContentType(ContentType.getInstance(( String ) xmlRpcParameters.get(REPOSITORY_CONTENTTYPE_IDX)));
            repository.setBaseUrl(( String ) xmlRpcParameters.get(REPOSITORY_BASE_URL_IDX));
            repository.setBaseRepositoryUrl(( String ) xmlRpcParameters.get(REPOSITORY_BASEREPO_URL_IDX));
            repository.setBaseTestUrl(( String ) xmlRpcParameters.get(REPOSITORY_BASETEST_URL_IDX));
            repository.setUsername(StringUtils.stripToNull(( String ) xmlRpcParameters.get(REPOSITORY_USERNAME_IDX)));
            repository.setPassword(StringUtils.stripToNull(( String ) xmlRpcParameters.get(REPOSITORY_PASSWORD_IDX)));
            repository.setMaxUsers(( Integer ) xmlRpcParameters.get(REPOSITORY_MAX_USERS_IDX));
        }

        return repository;
    }

    /**
     * Transforms the Vector of the Requirement parameters into a Requirement
     * Object.<br>
     * Structure of the parameters:<br>
     * Vector[name, Vector[repository parameters]]
     * <p>
     *
     * @param xmlRpcParameters
     * @return the Requirement.
     */
    @SuppressWarnings(SUPPRESS_UNCHECKED)
    public static Requirement toRequirement(Vector<Object> xmlRpcParameters) {
        Requirement requirement = null;
        if ( ! xmlRpcParameters.isEmpty()) {
            log.debug("Extracting requirement from XML-RPC parameters %s", xmlRpcParameters);
            String name = ( String ) xmlRpcParameters.get(DOCUMENT_NAME_IDX);
            requirement = Requirement.newInstance(name);
            requirement.setRepository(toRepository(( Vector<Object> ) xmlRpcParameters.get(DOCUMENT_REPOSITORY_IDX)));
        }

        return requirement;
    }

    /**
     * Transforms the Vector of the Specification parameters into a
     * Specification Object.<br>
     * Structure of the parameters:<br>
     * Vector[name, Vector[repository parameters]]
     * <p>
     *
     * @param xmlRpcParameters
     * @return the Specification.
     */
    @SuppressWarnings(SUPPRESS_UNCHECKED)
    public static Specification toSpecification(Vector<Object> xmlRpcParameters) {
        Specification specification = null;
        if ( ! xmlRpcParameters.isEmpty()) {
            log.debug("Extracting specification from XML-RPC parameters %s", xmlRpcParameters);
            specification = Specification.newInstance(( String ) xmlRpcParameters.get(DOCUMENT_NAME_IDX));
            specification.setRepository(toRepository(( Vector<Object> ) xmlRpcParameters.get(DOCUMENT_REPOSITORY_IDX)));
            specification.setTargetedSystemUnderTests(toSystemUnderTestList(( Vector<Object> ) xmlRpcParameters.get(
                SPECIFICATION_SUTS_IDX)));
        }

        return specification;
    }

    /**
     * Transforms the Vector of the Runner parameters into a Runner Object.<br>
     * <p>
     *
     * @param xmlRpcParameters
     * @return the Runner.
     */
    @SuppressWarnings(SUPPRESS_UNCHECKED)
    public static Runner toRunner(Vector<Object> xmlRpcParameters) {
        Runner runner = null;
        if ( ! xmlRpcParameters.isEmpty()) {
            log.debug("Extracting runner from XML-RPC parameters %s", xmlRpcParameters);
            runner = Runner.newInstance(( String ) getParameter(RUNNER_NAME_IDX, xmlRpcParameters));
            runner.setServerName(StringUtils.stripToNull(( String ) getParameter(RUNNER_SERVER_NAME_IDX, xmlRpcParameters)));
            runner.setServerPort(StringUtils.stripToNull(( String ) getParameter(RUNNER_SERVER_PORT_IDX, xmlRpcParameters)));
            runner.setSecured(( Boolean ) getParameter(RUNNER_SECURED_IDX, xmlRpcParameters));
            ClasspathSet classpaths = new ClasspathSet(( Vector<String> ) getParameter(RUNNER_CLASSPATH_IDX,
                xmlRpcParameters));
            runner.setClasspaths(classpaths);
        }

        return runner;
    }

    private static Object getParameter(int index, Vector<Object> parameters) {
        if (index > parameters.size() - 1) {
            return null;
        }
        return parameters.get(index);
    }

    /**
     * Transforms the Vector of the SystemUnderTest parameters into a
     * SystemUnderTest Object.<br>
     * Structure of the parameters:<br>
     * Vector[name, Vector[project parameters], Vector[seeds classPaths],
     * Vector[fixture classPaths], env]
     * <p>
     *
     * @param xmlRpcParameters
     * @return the SystemUnderTest.
     */
    @SuppressWarnings(SUPPRESS_UNCHECKED)
    public static SystemUnderTest toSystemUnderTest(Vector<Object> xmlRpcParameters) {
        SystemUnderTest sut = null;
        if ( ! xmlRpcParameters.isEmpty()) {
            log.debug("Extracting SUT from XML-RPC parameters %s", xmlRpcParameters);

            ClasspathSet sutClasspaths = new ClasspathSet(( Vector<String> ) xmlRpcParameters.get(SUT_CLASSPATH_IDX));
            ClasspathSet fixtureClasspaths = new ClasspathSet(( Vector<String> ) xmlRpcParameters.get(
                SUT_FIXTURE_CLASSPATH_IDX));

            sut = SystemUnderTest.newInstance(( String ) xmlRpcParameters.get(SUT_NAME_IDX));
            sut.setProject(toProject(( Vector<Object> ) xmlRpcParameters.get(SUT_PROJECT_IDX)));
            sut.setSutClasspaths(sutClasspaths);
            sut.setFixtureClasspaths(fixtureClasspaths);
            sut.setFixtureFactory(StringUtils.stripToNull(( String ) xmlRpcParameters.get(SUT_FIXTURE_FACTORY_IDX)));
            sut.setFixtureFactoryArgs(StringUtils.stripToNull(( String ) xmlRpcParameters.get(
                SUT_FIXTURE_FACTORY_ARGS_IDX)));
            sut.setRunner(toRunner(( Vector<Object> ) xmlRpcParameters.get(SUT_RUNNER_IDX)));
            sut.setIsDefault(( Boolean ) xmlRpcParameters.get(SUT_IS_DEFAULT_IDX));
            sut.setProjectDependencyDescriptor(StringUtils.stripToNull(( String ) xmlRpcParameters.get(
                SUT_PROJECT_DEPENDENCY_DESCRIPTOR_IDX)));
        }

        return sut;
    }

    /**
     * Transforms the Vector of the Reference parameters into a Reference
     * Object.<br>
     *
     * @param xmlRpcParameters
     * @return the Reference.
     */
    @SuppressWarnings(SUPPRESS_UNCHECKED)
    public static Reference toReference(Vector<Object> xmlRpcParameters) {
        Reference reference = null;
        if ( ! xmlRpcParameters.isEmpty()) {
            Requirement requirement = toRequirement(( Vector<Object> ) xmlRpcParameters.get(REFERENCE_REQUIREMENT_IDX));
            Specification specification = toSpecification(( Vector<Object> ) xmlRpcParameters.get(
                REFERENCE_SPECIFICATION_IDX));
            SystemUnderTest sut = toSystemUnderTest(( Vector<Object> ) xmlRpcParameters.get(REFERENCE_SUT_IDX));
            String sections = StringUtils.stripToNull(( String ) xmlRpcParameters.get(REFERENCE_SECTIONS_IDX));
            reference = Reference.newInstance(requirement, specification, sut, sections);
            Execution exe = toExecution(( Vector<Object> ) xmlRpcParameters.get(REFERENCE_LAST_EXECUTION_IDX));
            reference.setLastExecution(exe);
        }

        return reference;
    }

    public static Execution toExecution(Vector<Object> xmlRpcParameters) {
        Execution execution = new Execution();
        execution.setResults(StringUtils.stripToNull(( String ) xmlRpcParameters.get(EXECUTION_RESULTS_IDX)));
        execution.setExecutionErrorId(StringUtils.stripToNull(( String ) xmlRpcParameters.get(EXECUTION_ERRORID_IDX)));
        execution.setFailures(( Integer ) xmlRpcParameters.get(EXECUTION_FAILIURES_IDX));
        execution.setErrors(( Integer ) xmlRpcParameters.get(EXECUTION_ERRORS_IDX));
        execution.setSuccess(( Integer ) xmlRpcParameters.get(EXECUTION_SUCCESS_IDX));
        execution.setIgnored(( Integer ) xmlRpcParameters.get(EXECUTION_IGNORED_IDX));
        FormattedDate date = new FormattedDate(( String ) xmlRpcParameters.get(EXECUTION_EXECUTION_DATE_IDX));
        execution.setExecutionDate(date.asTimestamp());

        return execution;
    }

    public static RequirementSummary toRequirementSummary(Vector<Object> xmlRpcParameters) {
        RequirementSummary summary = new RequirementSummary();
        summary.setReferencesSize(( Integer ) xmlRpcParameters.get(SUMMARY_REFERENCES_IDX));
        summary.setFailures(( Integer ) xmlRpcParameters.get(SUMMARY_FAILIURES_IDX));
        summary.setErrors(( Integer ) xmlRpcParameters.get(SUMMARY_ERRORS_IDX));
        summary.setSuccess(( Integer ) xmlRpcParameters.get(SUMMARY_SUCCESS_IDX));
        summary.setExceptions(( Integer ) xmlRpcParameters.get(SUMMARY_EXCEPTION_IDX));

        return summary;
    }

    /**
     * Rebuild a List of projects based on the vector of projects parameters.
     * <p>
     *
     * @param projectsParams
     * @return a List of projects based on the vector of projects parameters.
     * @see #toProject(Vector)
     */
    @SuppressWarnings(SUPPRESS_UNCHECKED)
    public static Set<Project> toProjectList(Vector<Object> projectsParams) {
        Set<Project> projects = new TreeSet<Project>();
        for (Object projectParams : projectsParams) {
            projects.add(toProject(( Vector<Object> ) projectParams));
        }

        return projects;
    }

    /**
     * Rebuild a List of repositories based on the vector of repositories
     * parameters.
     * <p>
     *
     * @param repositoriesParams
     * @return a List of repositories based on the vector of repositories
     * parameters.
     * @see #toRepository(Vector)
     */
    @SuppressWarnings(SUPPRESS_UNCHECKED)
    public static Set<Repository> toRepositoryList(Vector<Object> repositoriesParams) {
        Set<Repository> repositories = new TreeSet<Repository>();
        for (Object repositoryParams : repositoriesParams) {
            repositories.add(toRepository(( Vector<Object> ) repositoryParams));
        }

        return repositories;
    }

    /**
     * Rebuild a List of runners based on the vector of runners parameters.
     * <p>
     *
     * @param runnersParams
     * @return a List of runners based on the vector of runners parameters.
     * @see #toRunner(Vector)
     */
    @SuppressWarnings(SUPPRESS_UNCHECKED)
    public static Set<Runner> toRunnerList(Vector<Object> runnersParams) {
        Set<Runner> runners = new TreeSet<Runner>();
        for (Object runnerParams : runnersParams) {
            runners.add(toRunner(( Vector<Object> ) runnerParams));
        }

        return runners;
    }

    /**
     * Rebuild a List of systemUnderTests based on the vector of
     * systemUnderTests parameters.
     * <p>
     *
     * @param sutsParams
     * @return a List of systemUnderTests based on the vector of
     * systemUnderTests parameters.
     * @see #toSystemUnderTest(Vector)
     */
    @SuppressWarnings(SUPPRESS_UNCHECKED)
    public static SortedSet<SystemUnderTest> toSystemUnderTestList(Vector<Object> sutsParams) {
        SortedSet<SystemUnderTest> suts = new TreeSet<SystemUnderTest>(new SystemUnderTestByNameComparator());
        for (Object sutParams : sutsParams) {
            suts.add(toSystemUnderTest(( Vector<Object> ) sutParams));
        }

        return suts;
    }

    /**
     * Rebuild a List of specifications based on the vector of specifications
     * parameters.
     * <p>
     *
     * @param specificationsParams
     * @return a List of specifications based on the vector of specifications
     * parameters.
     * @see #toSpecification(Vector)
     */
    @SuppressWarnings(SUPPRESS_UNCHECKED)
    public static Set<Specification> toSpecificationList(Vector<Object> specificationsParams) {
        Set<Specification> specifications = new TreeSet<Specification>();
        for (Object specificationParams : specificationsParams) {
            specifications.add(toSpecification(( Vector<Object> ) specificationParams));
        }

        return specifications;
    }

    /**
     * Rebuild a List of References with their last execution based on the
     * vector of References parameters.
     * <p>
     *
     * @param referencesParams
     * @return a List of References based on the vector of References
     * parameters.
     * @throws LivingDocServerException
     * @see #toReference(Vector)
     */
    @SuppressWarnings(SUPPRESS_UNCHECKED)
    public static Set<Reference> toReferencesList(Vector<Object> referencesParams) throws LivingDocServerException {
        Set<Reference> references = new TreeSet<Reference>();
        for (Object referenceParams : referencesParams) {
            references.add(toReference(( Vector<Object> ) referenceParams));
        }

        return references;
    }

    /**
     * Rebuilds a DocumentNode based on the given vector.
     * <p>
     *
     * @param documentNodeParams
     * @return a DocumentNode based on the given vector.
     */
    @SuppressWarnings(SUPPRESS_UNCHECKED)
    public static DocumentNode toDocumentNode(Vector< ? > documentNodeParams) {
        DocumentNode node = new DocumentNode(( String ) documentNodeParams.get(NODE_TITLE_INDEX));
        node.setIsExecutable(( Boolean ) documentNodeParams.get(NODE_EXECUTABLE_INDEX));
        node.setCanBeImplemented(( Boolean ) documentNodeParams.get(NODE_CAN_BE_IMPLEMENTED_INDEX));

        Map< ? , ? > children = ( Map< ? , ? > ) documentNodeParams.get(NODE_CHILDREN_INDEX);
        Iterator< ? > it = children.values().iterator();
        while (it.hasNext()) {
            Object next = it.next();
            Vector<Object> nodeParams = null;
            if (next.getClass().isArray()) {
                nodeParams = ClientUtils.vectorizeDeep(( Object[] ) next);
            } else {
                nodeParams = ( Vector<Object> ) next;
            }

            if (nodeParams.size() > 4) {
                node.addChildren(toReferenceNode(nodeParams));
            } else {
                node.addChildren(toDocumentNode(nodeParams));
            }
        }

        return node;
    }

    private static ReferenceNode toReferenceNode(Vector<Object> referenceNodeParams) {
        ReferenceNode node = new ReferenceNode(( String ) referenceNodeParams.get(NODE_TITLE_INDEX),
            ( String ) referenceNodeParams.get(NODE_REPOSITORY_UID_INDEX), ( String ) referenceNodeParams.get(
                NODE_SUT_NAME_INDEX), ( String ) referenceNodeParams.get(NODE_SECTION_INDEX));

        node.setIsExecutable(( Boolean ) referenceNodeParams.get(NODE_EXECUTABLE_INDEX));
        node.setCanBeImplemented(( Boolean ) referenceNodeParams.get(NODE_CAN_BE_IMPLEMENTED_INDEX));

        return node;
    }

    /**
     * Wraps the error message id into a String.<br>
     * Structure of the error:<br>
     * TAG_ERROR errorId
     * <p>
     *
     * @param msgId
     * @return the error message id as a String.
     */
    public static String errorAsString(String msgId) {
        return LivingDocServerErrorKey.ERROR + msgId;
    }

    /**
     * Wraps the error message id into a Vector of Vector of String.<br>
     * Structure of the error:<br>
     * Vector[Vector[TAG_ERROR errorId]]
     * <p>
     *
     * @param msgId
     * @return the error message id as a Vector.
     */
    public static Vector<Object> errorAsVector(String msgId) {
        Vector<Object> err = new Vector<Object>();
        err.add(errorAsString(msgId));
        return err;
    }

    /**
     * Wraps the error message id into a HashTable where exception is the key
     * and the value is a Vector of Vector of String.<br>
     * Structure of the error:<br>
     * HashTable[TAG_ERROR, Vector[Vector[TAG_ERROR errorId]]]
     * <p>
     *
     * @param msgId
     * @return the error message id as a HashTable.
     */
    public static Map<String, Vector<Object>> errorAsHastable(String msgId) {
        Map<String, Vector<Object>> table = new Hashtable<String, Vector<Object>>();
        table.put(LivingDocServerErrorKey.ERROR, errorAsVector(msgId));
        return table;
    }

    /**
     * Checks if the XML-RPC response is an server Exception. If so an
     * LivingDocServerException will be thrown with the error id found.
     * <p>
     *
     * @param xmlRpcResponse
     * @throws LivingDocServerException
     */
    @SuppressWarnings(SUPPRESS_UNCHECKED)
    public static void checkForErrors(Object xmlRpcResponse) throws LivingDocServerException {
        if (xmlRpcResponse instanceof Vector) {
            Vector< ? > temp = ( Vector< ? > ) xmlRpcResponse;
            if ( ! temp.isEmpty()) {
                checkErrors(temp.elementAt(0));
            }
        } else if (xmlRpcResponse instanceof Hashtable) {
            Hashtable<String, ? > table = ( Hashtable<String, ? > ) xmlRpcResponse;
            if ( ! table.isEmpty()) {
                checkForErrors(table.get(LivingDocServerErrorKey.ERROR));
            }
        } else {
            checkErrors(xmlRpcResponse);
        }
    }

    /**
     * Checks if the message is an server tagged Exception. If so an
     * LivingDocServerException will be thrown with the error id found.
     * <p>
     *
     * @param object the error id found.
     * @throws LivingDocServerException
     */
    private static void checkErrors(Object object) throws LivingDocServerException {
        if (object instanceof Exception) {
            throw new LivingDocServerException(LivingDocServerErrorKey.CALL_FAILED, ( ( Exception ) object ).getMessage());
        }

        if (object instanceof String) {
            String msg = ( String ) object;
            if ( ! StringUtils.isEmpty(msg) && msg.indexOf(LivingDocServerErrorKey.ERROR) > - 1) {
                String errorId = msg.replace(LivingDocServerErrorKey.ERROR, "");
                log.error(errorId);
                throw new LivingDocServerException(errorId, errorId);
            }
        }
    }
}
