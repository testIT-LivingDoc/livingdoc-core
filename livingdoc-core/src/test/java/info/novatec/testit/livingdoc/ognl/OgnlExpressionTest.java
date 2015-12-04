/* Copyright (c) 2006 Pyxis Technologies inc.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org. */

package info.novatec.testit.livingdoc.ognl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;


/**
 * Call class of <code>OgnlExpression</code>.
 */
public class OgnlExpressionTest {

    @Test(expected = UnresolvableExpressionException.class)
    public void testMalformedExpressionParsing() {
        OgnlExpression.onSingleExpression("()", new Root()).extractValue();
    }

    @Test(expected = UnresolvableExpressionException.class)
    public void testReferringPrivateProperty() {
        OgnlExpression ognl = OgnlExpression.onSingleExpression("privateStringField", new Root());
        ognl.extractValue();
    }

    @Test(expected = UnresolvableExpressionException.class)
    public void testReferringUnexistentProperty() {
        OgnlExpression ognl = OgnlExpression.onSingleExpression("UnexistentField", new Root());
        ognl.extractValue();
    }

    @Test
    public void testReferringPublicProperty() {
        OgnlExpression ognl = OgnlExpression.onSingleExpression("publicStringField", new Root());
        Object value = ognl.extractValue();
        assertEquals("value", value);
    }

    @Test
    public void testReferringGetterProperty() {
        OgnlExpression ognl = OgnlExpression.onSingleExpression("StringField", new Root());
        Object value = ognl.extractValue();
        assertEquals("value", value);
    }

    @Test
    public void testReferringNullProperty() {
        OgnlExpression ognl = OgnlExpression.onSingleExpression("nullField", new Root());
        Object value = ognl.extractValue();
        assertNull(value);
    }

    @Test
    public void testReferringChainedProperties() {
        OgnlExpression ognl = OgnlExpression.onSingleExpression("subRoot.field", new Root());
        Object value = ognl.extractValue();
        assertEquals("value", value);
    }

    @Test
    public void testCallingMethod() {
        OgnlExpression ognl = OgnlExpression.onSingleExpression("result()", new Root());
        Object value = ognl.extractValue();
        assertEquals(1, value);
    }

    @Test
    public void testCallingChainedMethods() {
        OgnlExpression ognl = OgnlExpression.onSingleExpression("subRoot().result()", new Root());
        Object value = ognl.extractValue();
        assertEquals("result", value);
    }

    @Test
    public void testCallingExpressionFromRightTarget() {
        OgnlExpression ognl = OgnlExpression.onSingleExpression("root2Field", new Root(), new Root2());
        Object value = ognl.extractValue();
        assertEquals("value", value);
    }

    @Test
    public void testSupportsConstantExpressions() {
        assertEquals(5, OgnlExpression.onSingleExpression("3 + 2").extractValue());
    }

    @Test
    public void testInsertValueInField() {
        Root root = new Root();
        OgnlExpression ognl = OgnlExpression.onSingleExpression("publicStringField", root);
        String valueInserted = "valueInserted";
        ognl.insertValue(valueInserted);
        Object value = ognl.extractValue();
        assertEquals(valueInserted, value);
    }

    @Test
    public void testInsertValueInProperty() {
        Root root = new Root();
        OgnlExpression ognl = OgnlExpression.onSingleExpression("StringField", root);
        String valueInserted = "valueInserted";
        ognl.insertValue(valueInserted);
        Object value = ognl.extractValue();
        assertEquals(valueInserted, value);
    }

    @Test
    public void testInsertValueInRightTarget() {
        Root root = new Root();
        Root2 root2 = new Root2();
        OgnlExpression ognl = OgnlExpression.onSingleExpression("root2Field", root, root2);
        String valueInserted = "valueInserted";
        ognl.insertValue(valueInserted);
        Object value = ognl.extractValue();
        assertEquals(valueInserted, value);
    }

    @Test(expected = UnresolvableExpressionException.class)
    public void testInsertValueInNotFoundExpressionFromTargets() {
        OgnlExpression ognl = OgnlExpression.onSingleExpression("unknown", new Root(), new Root2());
        ognl.insertValue("value");
    }

    @Test(expected = UnresolvableExpressionException.class)
    public void testInsertWrongFormatValue() {
        OgnlExpression ognl = OgnlExpression.onSingleExpression("publicIntField", new Root());
        ognl.insertValue("StringValue");
    }

    @SuppressWarnings("unused")
    private class Root {
        private String privateStringField = "value";
        private int privateIntField = 0;
        public String publicStringField = "value";
        public int publicIntField = 0;
        public String nullField = null;
        public SubRoot subRoot = new SubRoot();

        public void setStringField(String value) {
            privateStringField = value;
        }

        public String getStringField() {
            return privateStringField;
        }

        public void setIntField(int value) {
            privateIntField = value;
        }

        public int getIntField() {
            return privateIntField;
        }

        public int result() {
            return privateIntField + 1;
        }

        public SubRoot subRoot() {
            return new SubRoot();
        }
    }

    @SuppressWarnings("unused")
    private class SubRoot {
        private String field = "value";

        public String getField() {
            return field;
        }

        public String result() {
            return "result";
        }
    }

    @SuppressWarnings("unused")
    private class Root2 {
        public String root2Field = "value";
    }
}
