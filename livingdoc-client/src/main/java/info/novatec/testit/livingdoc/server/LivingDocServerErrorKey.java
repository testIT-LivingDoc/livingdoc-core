package info.novatec.testit.livingdoc.server;

public interface LivingDocServerErrorKey {
    public static final String SUCCESS = "<success>";
    public static final String ERROR = "<exception>";

    /**
     * General errors
     */
    public static final String GENERAL_ERROR = "livingdoc.server.generalerror";
    public static final String CONFIGURATION_ERROR = "livingdoc.server.configerror";
    public static final String XML_RPC_URL_NOTFOUND = "livingdoc.server.xmlrpcurlinvalid";
    public static final String XML_RPC_HANDLER_NOTFOUND = "livingdoc.server.xmlrpchandlerinvalid";
    public static final String NO_CONFIGURATION = "livingdoc.server.noconfiguration";
    public static final String CALL_FAILED = "livingdoc.server.callfailed";
    public static final String MARSHALL_NOT_SUPPORTED = "livingdoc.server.marshallingnotsupported";

    /**
     * Failed to retrieve.
     */
    public static final String RETRIEVE_PROJECTS = "livingdoc.server.retrieveprojects";
    public static final String RETRIEVE_REPOSITORIES = "livingdoc.server.retrieverepos";
    public static final String RETRIEVE_SPECIFICATION_REPOS = "livingdoc.server.retrievespecrepos";
    public static final String RETRIEVE_REQUIREMENT_REPOS = "livingdoc.server.retrieverequirementrepos";
    public static final String RETRIEVE_SUTS = "livingdoc.server.retrievesuts";
    public static final String RETRIEVE_COMPILATION = "livingdoc.server.retrievecompil";
    public static final String RETRIEVE_REFERENCES = "livingdoc.server.retrievereferences";
    public static final String RETRIEVE_EXECUTIONS = "livingdoc.server.retrieveexecutions";
    public static final String RETRIEVE_REFERENCE = "livingdoc.server.retrievereference";
    public static final String RETRIEVE_FILE_FAILED = "livingdoc.server.filefailed";

    /**
     * Project's errors.
     */
    public static final String PROJECT_NOT_FOUND = "livingdoc.server.projectnotfound";
    public static final String PROJECT_ALREADY_EXISTS = "livingdoc.server.projectalreadyexist";
    public static final String PROJECT_DEFAULT_SUT_NOT_FOUND = "livingdoc.server.defaultsutnotfound";
    public static final String PROJECT_REMOVE_FAILED = "livingdoc.server.removeprojectfailed";
    public static final String PROJECT_REPOSITORY_ASSOCIATED = "livingdoc.server.projectrepoassociated";
    public static final String PROJECT_SUTS_ASSOCIATED = "livingdoc.server.projectsutsassociated";

    /**
     * Repository's errors.
     */
    public static final String REPOSITORY_CLASS_NOT_FOUND = "livingdoc.server.repoclassnotfound";
    public static final String REPOSITORY_DOC_ASSOCIATED = "livingdoc.server.repodocassociated";
    public static final String REPOSITORY_NOT_FOUND = "livingdoc.server.repositorynotfound";
    public static final String REPOSITORY_UPDATE_FAILED = "livingdoc.server.repoupdatefailed";
    public static final String REPOSITORY_TYPE_NOT_FOUND = "livingdoc.server.rtypenotfound";
    public static final String PROJECT_CREATE_FAILED = "livingdoc.server.createprojectfailed";
    public static final String PROJECT_UPDATE_FAILED = "livingdoc.server.projectupdatefailed";
    public static final String REPOSITORY_ALREADY_EXISTS = "livingdoc.server.repoalreadyexists";
    public static final String REPOSITORY_REMOVE_FAILED = "livingdoc.server.removerepofailed";
    public static final String REPOSITORY_DOES_NOT_CONTAINS_SPECIFICATION = "livingdoc.repositorynotspecification";
    public static final String REPOSITORY_GET_REGISTERED = "livingdoc.server.retrieverepository";
    public static final String REPOSITORY_REGISTRATION_FAILED = "livingdoc.server.registrationfailed";
    public static final String REPOSITORY_UNREGISTRATION_FAILED = "livingdoc.server.unregistrationfailed";

    /**
     * Requirement's errors.
     */
    public static final String REQUIREMENT_NOT_FOUND = "livingdoc.server.requirementnotfound";
    public static final String REQUIREMENT_ALREADY_EXISTS = "livingdoc.server.requirementalreadyexists";
    public static final String REQUIREMENT_REMOVE_FAILED = "livingdoc.server.removerequirementfailed";

    /**
     * Specification's errors
     */
    public static final String SPECIFICATION_NOT_FOUND = "livingdoc.server.specificationnotfound";
    public static final String SPECIFICATIONS_NOT_FOUND = "livingdoc.server.specificationsnotfound";
    public static final String SPECIFICATION_CREATE_FAILED = "livingdoc.server.createspecificationfailed";
    public static final String SPECIFICATION_REFERENCED = "livingdoc.server.removereferencedspecification";
    public static final String SPECIFICATION_ALREADY_EXISTS = "livingdoc.server.specificationalreadyexists";
    public static final String SPECIFICATION_ADD_SUT_FAILED = "livingdoc.server.addsutspecificational";
    public static final String SPECIFICATION_REMOVE_SUT_FAILED = "livingdoc.server.removesutspecificational";
    public static final String SPECIFICATION_UPDATE_FAILED = "livingdoc.server.updatespecificationfailed";
    public static final String SPECIFICATION_REMOVE_FAILED = "livingdoc.server.removespecificationfailed";
    public static final String SPECIFICATION_RUN_FAILED = "livingdoc.server.runspecificationfailed";
    public static final String SPECIFICATION_IMPLEMENTED_FAILED = "livingdoc.server.implementedfailed";

    /**
     * Runners errors
     */
    public static final String RUNNER_ALREADY_EXISTS = "livingdoc.server.runneralreadyexists";
    public static final String RUNNERS_NOT_FOUND = "livingdoc.server.runnersnotfound";
    public static final String RUNNER_NOT_FOUND = "livingdoc.server.runnernotfound";
    public static final String RUNNER_CREATE_FAILED = "livingdoc.server.runnercreatefailed";
    public static final String RUNNER_UPDATE_FAILED = "livingdoc.server.runnerupdatefailed";
    public static final String RUNNER_REMOVE_FAILED = "livingdoc.server.runnerremovefailed";
    public static final String RUNNER_SUT_ASSOCIATED = "livingdoc.server.runnersutassociated";

    /**
     * System under test's error.
     */
    public static final String SUT_NOT_FOUND = "livingdoc.server.sutnotfound";
    public static final String SUT_REFERENCE_ASSOCIATED = "livingdoc.server.sutwithreferences";
    public static final String SUT_SPECIFICATION_ASSOCIATED = "livingdoc.server.sutwithspecifications";
    public static final String SUT_EXECUTION_ASSOCIATED = "livingdoc.server.sutwithexecutions";
    public static final String SUT_CREATE_FAILED = "livingdoc.server.createsutfailed";
    public static final String SUT_SET_DEFAULT_FAILED = "livingdoc.server.setdefaultsutfailed";
    public static final String SUT_ALREADY_EXISTS = "livingdoc.server.sutalreadyexists";
    public static final String SUT_UPDATE_FAILED = "livingdoc.server.updatesutfailed";
    public static final String SUT_DELETE_FAILED = "livingdoc.server.deletesutfailed";

    /**
     * Reference's errors.
     */
    public static final String REFERENCE_NOT_FOUND = "livingdoc.server.referencenotfound";
    public static final String REFERENCE_CREATE_FAILED = "livingdoc.server.createreferencefailed";
    public static final String REFERENCE_UPDATE_FAILED = "livingdoc.server.updatereferencefailed";
    public static final String REFERENCE_REMOVE_FAILED = "livingdoc.server.removereferencefailed";
    public static final String RUN_REFERENCE_FAILED = "livingdoc.server.runreferencefailed";
    public static final String REFERENCE_CREATE_ALREADYEXIST = "livingdoc.server.createreferencealreadyexist";

    /**
     * Execution's errors
     */
    public static final String EXECUTION_CREATE_FAILED = "livingdoc.server.createexecutionfailed";

    /** ????? */
    public static final String RESOLVED_URI_FAILED = "livingdoc.server.failedtoresolveuri";
}
