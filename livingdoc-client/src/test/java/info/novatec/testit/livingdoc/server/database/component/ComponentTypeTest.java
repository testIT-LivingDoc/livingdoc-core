package info.novatec.testit.livingdoc.server.database.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import info.novatec.testit.livingdoc.server.domain.component.ContentType;


public class ComponentTypeTest {
    @Test
    public void testThatTheInstanciateReturnsTheGoodInstance() {
        assertEquals(ContentType.BOTH, ContentType.getInstance("BOTH"));
        assertEquals(ContentType.TEST, ContentType.getInstance("TEST"));
        assertEquals(ContentType.REQUIREMENT, ContentType.getInstance("REQUIREMENT"));
        assertEquals(ContentType.UNKNOWN, ContentType.getInstance("TYPE-NOT-SUPPORTED"));
    }
}
