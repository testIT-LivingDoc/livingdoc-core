package info.novatec.testit.livingdoc.server.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;

import org.junit.Before;
import org.junit.Test;

import info.novatec.testit.livingdoc.report.XmlReport;
import info.novatec.testit.livingdoc.report.XmlReportMother;


public class ExecutionTest {
    private SystemUnderTest sut;
    private Requirement requirement;
    private Specification specification;
    XmlReport xmlReport;

    @Before
    public void setUp() throws Exception {
        Project project = Project.newInstance("PROJECT-1");
        Repository repository = Repository.newInstance("UID-1");
        repository.setType(RepositoryType.newInstance("TYPE-1"));
        project.addRepository(repository);

        sut = SystemUnderTest.newInstance("SUT-1");
        project.addSystemUnderTest(sut);
        requirement = Requirement.newInstance("REQUIREMENT-1");
        repository.addRequirement(requirement);
        specification = Specification.newInstance("SPECIFICATION-1");
        repository.addSpecification(specification);

        xmlReport = XmlReportMother.simpleResults();
    }

    @Test
    public void testThatTheExecutionsAreSortedByDescendingExecutionDate() {
        Execution currentExecution = Execution.newInstance(specification, sut, xmlReport);
        currentExecution.setExecutionDate(now());
        Execution newestExecution = Execution.newInstance(specification, sut, xmlReport);
        newestExecution.setExecutionDate(beforeNow());
        Execution oldestExecution = Execution.newInstance(specification, sut, xmlReport);
        oldestExecution.setExecutionDate(afterNow());

        assertEquals(0, currentExecution.compareTo(currentExecution));
        assertEquals(1, currentExecution.compareTo(newestExecution));
        assertEquals( - 1, currentExecution.compareTo(oldestExecution));
    }

    @Test
    public void testThatIfFailureOrExceptionOrErrorOccuresTestIsMarkedFAILED() {
        Execution execution = Execution.newInstance(specification, sut, xmlReport);
        execution.setErrors(1);
        execution.setFailures(0);
        execution.setSuccess(0);
        execution.setExecutionErrorId("");
        assertFalse(execution.wasIgnored());
        assertFalse(execution.hasSucceeded());
        assertFalse(execution.hasException());
        assertTrue(execution.hasFailed());
        assertEquals(Execution.FAILED, execution.getStatus());

        execution.setErrors(0);
        execution.setFailures(1);
        execution.setSuccess(0);
        execution.setExecutionErrorId("");
        assertFalse(execution.wasIgnored());
        assertFalse(execution.hasSucceeded());
        assertFalse(execution.hasException());
        assertTrue(execution.hasFailed());
        assertEquals(Execution.FAILED, execution.getStatus());

        execution.setErrors(0);
        execution.setFailures(0);
        execution.setSuccess(0);
        execution.setExecutionErrorId("EXCEPTION THROWN");
        assertFalse(execution.wasIgnored());
        assertFalse(execution.hasSucceeded());
        assertTrue(execution.hasFailed());
        assertTrue(execution.hasException());
        assertEquals(Execution.FAILED, execution.getStatus());
    }

    @Test
    public void testThatIfNoFailureOrExceptionOrSuccessOrErrorOccuresTestIsMarkedIGNORED() {
        Execution execution = Execution.newInstance(specification, sut, xmlReport);

        execution.setErrors(0);
        execution.setFailures(0);
        execution.setSuccess(0);
        execution.setIgnored(5);
        execution.setExecutionErrorId("");
        assertFalse(execution.hasFailed());
        assertFalse(execution.hasException());
        assertFalse(execution.hasSucceeded());
        assertTrue(execution.wasIgnored());
        assertTrue(execution.wasRunned());
        assertEquals(Execution.IGNORED, execution.getStatus());
    }

    @Test
    public void testThatIfNoFailureOrExceptionOrSuccessOrIgnoredOrErrorOccuresTestIsMarkedNOT_RUNNED() {
        Execution execution = Execution.newInstance(specification, sut, xmlReport);

        execution.setErrors(0);
        execution.setFailures(0);
        execution.setSuccess(0);
        execution.setIgnored(0);
        execution.setExecutionErrorId("");
        assertFalse(execution.hasFailed());
        assertFalse(execution.hasException());
        assertFalse(execution.hasSucceeded());
        assertFalse(execution.wasIgnored());
        assertFalse(execution.wasRunned());
        assertEquals(Execution.NOT_RUNNED, execution.getStatus());
    }

    @Test
    public void testThatIfOnlySuccessOccuresTestIsMarkedSUCCESSED() {
        Execution execution = Execution.newInstance(specification, sut, xmlReport);

        execution.setErrors(0);
        execution.setFailures(0);
        execution.setSuccess(1);
        execution.setExecutionErrorId("");
        assertFalse(execution.wasIgnored());
        assertFalse(execution.hasFailed());
        assertFalse(execution.hasException());
        assertTrue(execution.hasSucceeded());
        assertEquals(Execution.SUCCESS, execution.getStatus());
    }

    private Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    private Timestamp beforeNow() {
        return new Timestamp(System.currentTimeMillis() - 1000);
    }

    private Timestamp afterNow() {
        return new Timestamp(System.currentTimeMillis() + 1000);
    }
}
