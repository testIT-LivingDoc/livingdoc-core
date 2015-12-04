package info.novatec.testit.livingdoc.reflect;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import info.novatec.testit.livingdoc.fixture.converter.TypeConversionFixture;
import info.novatec.testit.livingdoc.fixture.converter.TypeConversionFixture.TypeNoConverterWithSelf;
import info.novatec.testit.livingdoc.fixture.converter.TypeConversionFixture.TypeNoConverterWithSelfByValueOf;
import info.novatec.testit.livingdoc.fixture.interpreter.InterpretationOrderFixture;


@SuppressWarnings("unused")
public class AnnotationLoaderTest {
    private static AnnotationLoader< ? > annoLoader;
    private static ClassLoader classLoader;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @SuppressWarnings("rawtypes")
    @BeforeClass
    public static void setup() {
        annoLoader = new AnnotationLoader();
        classLoader = AnnotationLoader.class.getClassLoader();
        annoLoader.addLoader(classLoader);
    }

    @Test
    public void scanForAnnotatedFixture() {
        Type< ? > type = annoLoader.getAnnotatedFixture("Type Conversion");

        assertEquals(TypeConversionFixture.class, type.getUnderlyingClass());
    }

    @Test
    public void scanForInnerClasses() {
        Type< ? > type = annoLoader.getAnnotatedFixture("Type No Converter With Self");

        assertEquals(TypeNoConverterWithSelf.class, type.getUnderlyingClass());

        Type< ? > type2 = annoLoader.getAnnotatedFixture("TypeNoConverterWithSelfByValueOf");

        assertEquals(TypeNoConverterWithSelfByValueOf.class, type2.getUnderlyingClass());
    }

    @Test
    public void scanForAlias() {
        Type< ? > type = annoLoader.getAnnotatedFixture("Interpretationsreihenfolge");

        assertEquals(InterpretationOrderFixture.class, type.getUnderlyingClass());
    }

    @Test
    public void throwsDuplicateFixtureError() {
        thrown.expect(DuplicateAnnotatedFixturesFoundException.class);
        thrown.expectMessage("Following annotated");
        Type< ? > type = annoLoader.getAnnotatedFixture("MockFixture");
    }

    @Test
    public void duplicateAliasThrowsError() {
        thrown.expect(DuplicateAnnotatedFixturesFoundException.class);
        thrown.expectMessage("Following aliases");
        Type< ? > type = annoLoader.getAnnotatedFixture("MockAliasTest");
    }
}
