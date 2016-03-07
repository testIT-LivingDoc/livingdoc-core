package info.novatec.testit.livingdoc.server;

public interface LivingDocServerErrorKey {
    String SUCCESS = "<success>";
    String ERROR = "<exception>";

    /**
     * General errors
     */
    String GENERAL_ERROR = "livingdoc.server.generalerror";
    String CONFIGURATION_ERROR = "livingdoc.server.configerror";
    String XML_RPC_URL_NOTFOUND = "livingdoc.server.xmlrpcurlinvalid";
    String XML_RPC_HANDLER_NOTFOUND = "livingdoc.server.xmlrpchandlerinvalid";
    String NO_CONFIGURATION = "livingdoc.server.noconfiguration";
    String CALL_FAILED = "livingdoc.server.callfailed";
    String MARSHALL_NOT_SUPPORTED = "livingdoc.server.marshallingnotsupported";

    /**
     * Failed to retrieve.
     */
    String RETRIEVE_PROJECTS = "livingdoc.server.retrieveprojects";
    String RETRIEVE_REPOSITORIES = "livingdoc.server.retrieverepos";
    String RETRIEVE_SPECIFICATION_REPOS = "livingdoc.server.retrievespecrepos";
    String RETRIEVE_REQUIREMENT_REPOS = "livingdoc.server.retrieverequirementrepos";
    String RETRIEVE_SUTS = "livingdoc.server.retrievesuts";
    String RETRIEVE_COMPILATION = "livingdoc.server.retrievecompil";
    String RETRIEVE_REFERENCES = "livingdoc.server.retrievereferences";
    String RETRIEVE_EXECUTIONS = "livingdoc.server.retrieveexecutions";
    String RETRIEVE_REFERENCE = "livingdoc.server.retrievereference";
    String RETRIEVE_FILE_FAILED = "livingdoc.server.filefailed";

    /**
     * Project's errors.
     */
    String PROJECT_NOT_FOUND = "livingdoc.server.projectnotfound";
    String PROJECT_ALREADY_EXISTS = "livingdoc.server.projectalreadyexist";
    String PROJECT_DEFAULT_SUT_NOT_FOUND = "livingdoc.server.defaultsutnotfound";
    String PROJECT_REMOVE_FAILED = "livingdoc.server.removeprojectfailed";
    String PROJECT_REPOSITORY_ASSOCIATED = "livingdoc.server.projectrepoassociated";
    String PROJECT_SUTS_ASSOCIATED = "livingdoc.server.projectsutsassociated";

    /**
     * Repository's errors.
     */
    String REPOSITORY_CLASS_NOT_FOUND = "livingdoc.server.repoclassnotfound";
    String REPOSITORY_DOC_ASSOCIATED = "livingdoc.server.repodocassociated";
    String REPOSITORY_NOT_FOUND = "livingdoc.server.repositorynotfound";
    String REPOSITORY_UPDATE_FAILED = "livingdoc.server.repoupdatefailed";
    String REPOSITORY_TYPE_NOT_FOUND = "livingdoc.server.rtypenotfound";
    String PROJECT_CREATE_FAILED = "livingdoc.server.createprojectfailed";
    String PROJECT_UPDATE_FAILED = "livingdoc.server.projectupdatefailed";
    String REPOSITORY_ALREADY_EXISTS = "livingdoc.server.repoalreadyexists";
    String REPOSITORY_REMOVE_FAILED = "livingdoc.server.removerepofailed";
    String REPOSITORY_DOES_NOT_CONTAINS_SPECIFICATION = "livingdoc.repositorynotspecification";
    String REPOSITORY_GET_REGISTERED = "livingdoc.server.retrieverepository";
    String REPOSITORY_REGISTRATION_FAILED = "livingdoc.server.registrationfailed";
    String REPOSITORY_UNREGISTRATION_FAILED = "livingdoc.server.unregistrationfailed";

    /**
     * Requirement's errors.
     */
    String REQUIREMENT_NOT_FOUND = "livingdoc.server.requirementnotfound";
    String REQUIREMENT_ALREADY_EXISTS = "livingdoc.server.requirementalreadyexists";
    String REQUIREMENT_REMOVE_FAILED = "livingdoc.server.removerequirementfailed";

    /**
     * Specification's errors
     */
    String SPECIFICATION_NOT_FOUND = "livingdoc.server.specificationnotfound";
    String SPECIFICATIONS_NOT_FOUND = "livingdoc.server.specificationsnotfound";
    String SPECIFICATION_CREATE_FAILED = "livingdoc.server.createspecificationfailed";
    String SPECIFICATION_REFERENCED = "livingdoc.server.removereferencedspecification";
    String SPECIFICATION_ALREADY_EXISTS = "livingdoc.server.specificationalreadyexists";
    String SPECIFICATION_ADD_SUT_FAILED = "livingdoc.server.addsutspecificational";
    String SPECIFICATION_REMOVE_SUT_FAILED = "livingdoc.server.removesutspecificational";
    String SPECIFICATION_UPDATE_FAILED = "livingdoc.server.updatespecificationfailed";
    String SPECIFICATION_REMOVE_FAILED = "livingdoc.server.removespecificationfailed";
    String SPECIFICATION_RUN_FAILED = "livingdoc.server.runspecificationfailed";
    String SPECIFICATION_IMPLEMENTED_FAILED = "livingdoc.server.implementedfailed";

    /**
     * Runners errors
     */
    String RUNNER_ALREADY_EXISTS = "livingdoc.server.runneralreadyexists";
    String RUNNERS_NOT_FOUND = "livingdoc.server.runnersnotfound";
    String RUNNER_NOT_FOUND = "livingdoc.server.runnernotfound";
    String RUNNER_CREATE_FAILED = "livingdoc.server.runnercreatefailed";
    String RUNNER_UPDATE_FAILED = "livingdoc.server.runnerupdatefailed";
    String RUNNER_REMOVE_FAILED = "livingdoc.server.runnerremovefailed";
    String RUNNER_SUT_ASSOCIATED = "livingdoc.server.runnersutassociated";

    /**
     * System under test's error.
     */
    String SUT_NOT_FOUND = "livingdoc.server.sutnotfound";
    String SUT_REFERENCE_ASSOCIATED = "livingdoc.server.sutwithreferences";
    String SUT_SPECIFICATION_ASSOCIATED = "livingdoc.server.sutwithspecifications";
    String SUT_EXECUTION_ASSOCIATED = "livingdoc.server.sutwithexecutions";
    String SUT_CREATE_FAILED = "livingdoc.server.createsutfailed";
    String SUT_SET_DEFAULT_FAILED = "livingdoc.server.setdefaultsutfailed";
    String SUT_ALREADY_EXISTS = "livingdoc.server.sutalreadyexists";
    String SUT_UPDATE_FAILED = "livingdoc.server.updatesutfailed";
    String SUT_DELETE_FAILED = "livingdoc.server.deletesutfailed";

    /**
     * Reference's errors.
     */
    String REFERENCE_NOT_FOUND = "livingdoc.server.referencenotfound";
    String REFERENCE_CREATE_FAILED = "livingdoc.server.createreferencefailed";
    String REFERENCE_UPDATE_FAILED = "livingdoc.server.updatereferencefailed";
    String REFERENCE_REMOVE_FAILED = "livingdoc.server.removereferencefailed";
    String RUN_REFERENCE_FAILED = "livingdoc.server.runreferencefailed";
    String REFERENCE_CREATE_ALREADYEXIST = "livingdoc.server.createreferencealreadyexist";

    /**
     * Execution's errors
     */
    String EXECUTION_CREATE_FAILED = "livingdoc.server.createexecutionfailed";

    /** ????? */
    String RESOLVED_URI_FAILED = "livingdoc.server.failedtoresolveuri";
}
