package info.novatec.testit.livingdoc.server.rest;

public enum RestMethodName {
    testConnection,

    ping,
    getAllEnvironmentTypes,
    getRunner,
    getAllRunners,
    createRunner,
    updateRunner,
    removeRunner,
    getRegisteredRepository,
    registerRepository,
    updateRepositoryRegistration,
    removeRepository,
    getAllProjects,

    getAllSpecificationRepositories,
    getAllRepositoriesForSystemUnderTest,
    getSpecificationRepositoriesOfAssociatedProject,
    getSpecificationRepositoriesForSystemUnderTest,
    getRequirementRepositoriesOfAssociatedProject,
    getSystemUnderTestsOfAssociatedProject,
    getSystemUnderTestsOfProject,
    addSpecificationSystemUnderTest,
    removeSpecificationSystemUnderTest,

    doesSpecificationHasReferences,
    getSpecificationReferences,
    doesRequirementHasReferences,
    getRequirementReferences,
    getRequirementSummary,

    getReference,
    createReference,
    updateReference,
    removeReference,
    runReference,

    getSystemUnderTest,
    createSystemUnderTest,
    updateSystemUnderTest,
    removeSystemUnderTest,
    setSystemUnderTestAsDefault,

    removeRequirement,
    runSpecification,
    getSpecification,
    createSpecification,
    updateSpecification,
    removeSpecification,
    getListOfSpecificationLocations,
    getSpecificationHierarchy,
    setSpecificationAsImplemented,
    listDocumentsInHierarchy,
    getRenderedSpecification,
    saveExecutionResult;
}
