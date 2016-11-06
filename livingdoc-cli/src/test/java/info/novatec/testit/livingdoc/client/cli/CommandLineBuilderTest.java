package info.novatec.testit.livingdoc.client.cli;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class CommandLineBuilderTest {

   @Test
    public void testWeIgnoreSingleWhitespacesinCommandLine(){
        String[] expected = new String[]{"a", "b", "c" };
        assertArrayEquals(expected, new CommandLineBuilder(" a b c ").getCmdLine());
    }
   @Test
   public void testWeIgnoreMultipleWhitespacesinCommandLine(){
       String[] expected = new String[]{"a", "b", "c" };
       assertArrayEquals(expected, new CommandLineBuilder("a   b    c  ").getCmdLine());
   }

}
