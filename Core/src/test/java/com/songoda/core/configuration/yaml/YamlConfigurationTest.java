package com.songoda.core.configuration.yaml;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class YamlConfigurationTest {
    static final String inputYaml = "foo: bar\n" +
            "primitives:\n" +
            "  int: " + Integer.MIN_VALUE + "\n" +
            "  long: " + Long.MIN_VALUE + "\n" +
            "  float: " + Float.MIN_VALUE + "\n" +
            "  double: " + Double.MIN_VALUE + "\n" +
            "  char: ä\n" +
            "  string: string\n" +
            "  string-long: " + StringUtils.repeat("abc", 512) + "\n" +
            "  string-multi-line: |\n" +
            "                a\n" +
            "                b\n" +
            "                c\n" +
            "  boolean: true\n" +
            "  list: [2, 1, 3]\n" +
            "  map:\n" +
            "    key: value\n" +
            "  set:\n" +
            "    - 1\n" +
            "    - 2\n" +
            "    - 3\n";
    static final String expectedOutYaml = "foo: bar\n" +
            "primitives:\n" +
            "  int: " + Integer.MIN_VALUE + "\n" +
            "  long: " + Long.MIN_VALUE + "\n" +
            "  float: " + Float.MIN_VALUE + "\n" +
            "  double: " + Double.MIN_VALUE + "\n" +
            "  char: ä\n" +
            "  string: string\n" +
            "  string-long: " + StringUtils.repeat("abc", 512) + "\n" +
            "  string-multi-line: |\n" +
            "    a\n" +
            "    b\n" +
            "    c\n" +
            "  boolean: true\n" +
            "  list:\n" +
            "    - 2\n" +
            "    - 1\n" +
            "    - 3\n" +
            "  map:\n" +
            "    key: value\n" +
            "  set:\n" +
            "    - 1\n" +
            "    - 2\n" +
            "    - 3\n";

    @Test
    void testYamlParser() throws IOException {
        final YamlConfiguration cfg = new YamlConfiguration();
        cfg.load(new StringReader(inputYaml));

        assertEquals(Integer.MIN_VALUE, cfg.get("primitives.int"));
        assertEquals(Long.MIN_VALUE, cfg.get("primitives.long"));
        assertEquals(Float.MIN_VALUE, ((Number) cfg.get("primitives.float")).floatValue());
        assertEquals(Double.MIN_VALUE, cfg.get("primitives.double"));

        assertEquals("ä", cfg.get("primitives.char"));

        assertEquals("string", cfg.get("primitives.string"));

        assertInstanceOf(Boolean.class, cfg.get("primitives.boolean"));
        assertTrue((Boolean) cfg.get("primitives.boolean"));

        List<?> primitivesList = (List<?>) cfg.get("primitives.list");
        assertNotNull(primitivesList);
        assertInstanceOf(List.class, cfg.get("primitives.list"));
        assertEquals(3, primitivesList.size());
        assertEquals(2, primitivesList.get(0));
        assertEquals(1, primitivesList.get(1));
        assertEquals(3, primitivesList.get(2));

        assertEquals("value", cfg.get("primitives.map.key"));

        assertInstanceOf(List.class, cfg.get("primitives.set"));
        assertEquals(3, ((List<?>) cfg.get("primitives.set")).size());
    }

    @Test
    void testYamlParserWithEmptyFile() throws IOException {
        final YamlConfiguration cfg = new YamlConfiguration();
        cfg.load(new StringReader(""));
        assertTrue(cfg.getKeys("").isEmpty());

        cfg.load(new StringReader("\n"));
        assertTrue(cfg.getKeys("").isEmpty());
    }

    @Test
    void testYamlParserWithDuplicateKeys() {
        assertThrowsExactly(DuplicateKeyException.class,
                () -> new YamlConfiguration().load(new StringReader("test: value1\ntest: value2")));
    }

    @Test
    void testYamlParserWithInvalidReader() throws IOException {
        Reader reader = new StringReader("");
        reader.close();

        assertThrowsExactly(YAMLException.class, () -> new YamlConfiguration().load(reader));
    }

    @Test
    void testYamlWriter() throws IOException {
        final YamlConfiguration cfg = new YamlConfiguration();
        final StringWriter stringWriter = new StringWriter(inputYaml.length());

        cfg.load(new StringReader(inputYaml));
        cfg.save(stringWriter);

        assertEquals(expectedOutYaml, stringWriter.toString());
        assertEquals(expectedOutYaml, cfg.toYamlString());
    }

    @Test
    void testYamlWriterWithNullValue() throws IOException {
        final YamlConfiguration cfg = new YamlConfiguration();
        final StringWriter stringWriter = new StringWriter(1);

        cfg.set("null-value", null);
        cfg.set("nested.null-value", null);
        cfg.save(stringWriter);

        assertEquals("", stringWriter.toString());
        assertEquals("", cfg.toYamlString());
    }

    @Test
    void testYamlWriterWithNoData() throws IOException {
        final YamlConfiguration cfg = new YamlConfiguration();
        final StringWriter stringWriter = new StringWriter(inputYaml.length());

        cfg.save(stringWriter);

        assertEquals("", stringWriter.toString());
        assertEquals("", cfg.toYamlString());
    }

    @Test
    void testYamlWriterWithNoDataAndComments() throws IOException {
        final YamlConfiguration cfg = new YamlConfiguration();
        final StringWriter stringWriter = new StringWriter(inputYaml.length());

        cfg.setHeaderComment("baz");
        cfg.setNodeComment("foo", "bar");

        cfg.save(stringWriter);

        assertEquals("# baz\n", stringWriter.toString());
        assertEquals("# baz\n", cfg.toYamlString());
    }

    @Test
    void testSetter() {
        final YamlConfiguration cfg = new YamlConfiguration();

        assertNull(cfg.set("foo.bar.innerBar", "bar")); // 'foo.bar' gets overwritten

        Object prevValue = cfg.set("foo.bar", "baz");
        assertInstanceOf(Map.class, prevValue);
        assertEquals(1, ((Map<?, ?>) prevValue).size());
        assertEquals("bar", ((Map<?, ?>) prevValue).get("innerBar"));

        assertNull(cfg.set("number", 27));
        assertNull(cfg.set("bar.foo.faa1", "value1"));
        assertNull(cfg.set("bar.foo.faa2", "value2"));

        assertFalse(cfg.has("a.b.c"));
        assertFalse(cfg.has("a"));

        Map<String, Object> expectedValues = new HashMap<String, Object>() {{
            put("number", 27);

            put("foo", new HashMap<String, Object>() {{
                put("bar", "baz");
            }});

            put("bar", new HashMap<String, Object>() {{
                put("foo", new HashMap<String, Object>() {{
                    put("faa1", "value1");
                    put("faa2", "value2");
                }});
            }});
        }};

        assertEquals(expectedValues, cfg.values);
    }

    @Test
    void testSetterAndGetterWithPrimitiveValues() {
        final YamlConfiguration cfg = new YamlConfiguration();

        assertNull(cfg.set("foobar", "test"));
        assertNull(cfg.set("foo.bar", "test2"));
        assertEquals("test", cfg.set("foobar", "overwritten-test"));

        assertEquals("overwritten-test", cfg.get("foobar"));

        assertEquals("test2", cfg.get("foo.bar"));

        assertNull(cfg.set("primitives.int", Integer.MIN_VALUE));
        assertNull(cfg.set("primitives.long", Long.MIN_VALUE));
        assertNull(cfg.set("primitives.float", Float.MIN_VALUE));
        assertNull(cfg.set("primitives.double", Double.MIN_VALUE));
        assertNull(cfg.set("primitives.char", 'ä'));
        assertNull(cfg.set("primitives.string", "string"));
        assertNull(cfg.set("primitives.boolean", true));

        assertEquals(Integer.MIN_VALUE, cfg.get("primitives.int"));
        assertEquals(Long.MIN_VALUE, cfg.get("primitives.long"));

        assertInstanceOf(Double.class, cfg.get("primitives.float"));
        assertEquals(Float.MIN_VALUE, ((Number) cfg.get("primitives.float")).floatValue());

        assertEquals(Double.MIN_VALUE, cfg.get("primitives.double"));

        assertInstanceOf(String.class, cfg.get("primitives.char"));
        assertEquals("ä", cfg.get("primitives.char"));

        assertEquals("string", cfg.get("primitives.string"));
        assertInstanceOf(Boolean.class, cfg.get("primitives.boolean"));
        assertTrue((Boolean) cfg.get("primitives.boolean"));

        assertNull(cfg.set("primitives.map.key", "value"));
        assertEquals("value", cfg.get("primitives.map.key"));
    }

    @Test
    void testSetterWithNullValue() {
        final YamlConfiguration cfg = new YamlConfiguration();

        assertNull(cfg.set("foo.bar.null", "not-null-string"));
        assertEquals("not-null-string", cfg.set("foo.bar.null", null));

        assertNull(cfg.get("foo.bar.null"));
    }

    @Test
    void testGetNonExistingNestedKey() throws IOException {
        final YamlConfiguration cfg = new YamlConfiguration();
        cfg.load(new StringReader(inputYaml));

        assertNull(cfg.get("primitives.map2.key"));
    }

    @Test
    void testGetOrDefault() throws IOException {
        final YamlConfiguration cfg = new YamlConfiguration();
        cfg.load(new StringReader(inputYaml));

        assertEquals("bar", cfg.set("foo", "bar"));
        assertNull(cfg.set("bar.baz", "foz"));

        assertEquals("bar", cfg.getOr("foo", "baz"));
        assertEquals("foz", cfg.getOr("bar.baz", "baz"));

        assertEquals("default", cfg.getOr("foo.bar", "default"));
        assertEquals("default", cfg.getOr("bar.baz.foo", "default"));
    }

    @Test
    void testGetterWithNullKey() {
        final YamlConfiguration cfg = new YamlConfiguration();

        assertNull(cfg.get(null));
    }

    @Test
    void testGetKeys() throws IOException {
        final YamlConfiguration cfg = new YamlConfiguration();
        cfg.load(new StringReader(inputYaml));

        assertEquals(2, cfg.getKeys("").size());
        assertTrue(cfg.getKeys(null).isEmpty());

        assertTrue(cfg.getKeys("primitives.map.key.non-existing-subkey").isEmpty());
        assertTrue(cfg.getKeys("foo").isEmpty());

        assertArrayEquals(new String[] {"key"}, cfg.getKeys("primitives.map").toArray());
        assertArrayEquals(new String[] {"int", "long", "float", "double", "char", "string", "string-long", "string-multi-line", "boolean", "list", "map", "set"}, cfg.getKeys("primitives").toArray());
    }

    @Test
    void testSetterWithListValues() {
        final YamlConfiguration cfg = new YamlConfiguration();

        assertNull(cfg.set("primitives.list", Arrays.asList(2, 1, 3)));

        assertInstanceOf(List.class, cfg.get("primitives.list"));
        List<?> primitivesList = (List<?>) cfg.get("primitives.list");
        assertNotNull(primitivesList);
        assertEquals(3, primitivesList.size());
        assertEquals(2, primitivesList.get(0));
        assertEquals(1, primitivesList.get(1));
        assertEquals(3, primitivesList.get(2));
    }

    @Test
    void testSetterWithEnumValue() {
        final YamlConfiguration cfg = new YamlConfiguration();

        assertNull(cfg.set("primitives.enum", TestEnum.ENUM_VALUE));

        assertInstanceOf(String.class, cfg.get("primitives.enum"));
        assertEquals(TestEnum.ENUM_VALUE, TestEnum.valueOf((String) cfg.get("primitives.enum")));
    }

    @Test
    void testSetterWithBooleanArrayValue() {
        final YamlConfiguration cfg = new YamlConfiguration();

        assertNull(cfg.set("primitives.array", new boolean[] {Boolean.FALSE, Boolean.TRUE}));

        assertInstanceOf(List.class, cfg.get("primitives.array"));
        List<?> primitivesList = (List<?>) cfg.get("primitives.array");
        assert primitivesList != null;
        assertEquals(2, primitivesList.size());
        assertEquals(Boolean.FALSE, primitivesList.get(0));
        assertEquals(Boolean.TRUE, primitivesList.get(1));
    }

    @Test
    void testSetterWithByteArrayValue() {
        final YamlConfiguration cfg = new YamlConfiguration();

        assertNull(cfg.set("primitives.array", new byte[] {2, Byte.MIN_VALUE, Byte.MAX_VALUE}));

        assertInstanceOf(List.class, cfg.get("primitives.array"));
        List<?> primitivesList = (List<?>) cfg.get("primitives.array");
        assert primitivesList != null;
        assertEquals(3, primitivesList.size());
        assertEquals(2, primitivesList.get(0));
        assertEquals((int) Byte.MIN_VALUE, primitivesList.get(1));
        assertEquals((int) Byte.MAX_VALUE, primitivesList.get(2));
    }

    @Test
    void testSetterWithCharArrayValue() {
        final YamlConfiguration cfg = new YamlConfiguration();

        assertNull(cfg.set("primitives.array", new char[] {'x', Character.MIN_VALUE, Character.MAX_VALUE}));

        assertInstanceOf(List.class, cfg.get("primitives.array"));
        List<?> primitivesList = (List<?>) cfg.get("primitives.array");
        assert primitivesList != null;
        assertEquals(3, primitivesList.size());
        assertEquals("x", primitivesList.get(0));
        assertEquals(String.valueOf(Character.MIN_VALUE), primitivesList.get(1));
        assertEquals(String.valueOf(Character.MAX_VALUE), primitivesList.get(2));
    }

    @Test
    void testSetterWithShortArrayValue() {
        final YamlConfiguration cfg = new YamlConfiguration();

        assertNull(cfg.set("primitives.array", new short[] {2, Short.MIN_VALUE, Short.MAX_VALUE}));

        assertInstanceOf(List.class, cfg.get("primitives.array"));
        List<?> primitivesList = (List<?>) cfg.get("primitives.array");
        assert primitivesList != null;
        assertEquals(3, primitivesList.size());
        assertEquals(2, primitivesList.get(0));
        assertEquals((int) Short.MIN_VALUE, primitivesList.get(1));
        assertEquals((int) Short.MAX_VALUE, primitivesList.get(2));
    }

    @Test
    void testSetterWithIntArrayValue() {
        final YamlConfiguration cfg = new YamlConfiguration();

        assertNull(cfg.set("primitives.array", new int[] {2, Integer.MIN_VALUE, Integer.MAX_VALUE}));

        assertInstanceOf(List.class, cfg.get("primitives.array"));
        List<?> primitivesList = (List<?>) cfg.get("primitives.array");
        assert primitivesList != null;
        assertEquals(3, primitivesList.size());
        assertEquals(2, primitivesList.get(0));
        assertEquals(Integer.MIN_VALUE, primitivesList.get(1));
        assertEquals(Integer.MAX_VALUE, primitivesList.get(2));
    }

    @Test
    void testSetterWithLongArrayValue() {
        final YamlConfiguration cfg = new YamlConfiguration();

        assertNull(cfg.set("primitives.array", new long[] {2, Long.MIN_VALUE, Long.MAX_VALUE}));

        assertInstanceOf(List.class, cfg.get("primitives.array"));
        List<?> primitivesList = (List<?>) cfg.get("primitives.array");
        assert primitivesList != null;
        assertEquals(3, primitivesList.size());
        assertEquals((long) 2, primitivesList.get(0));
        assertEquals(Long.MIN_VALUE, primitivesList.get(1));
        assertEquals(Long.MAX_VALUE, primitivesList.get(2));
    }

    @Test
    void testSetterWithFloatArrayValue() {
        final YamlConfiguration cfg = new YamlConfiguration();

        assertNull(cfg.set("primitives.array", new float[] {2, Float.MIN_VALUE, Float.MAX_VALUE}));

        assertInstanceOf(List.class, cfg.get("primitives.array"));
        List<?> primitivesList = (List<?>) cfg.get("primitives.array");
        assert primitivesList != null;
        assertEquals(3, primitivesList.size());
        assertEquals((double) 2, primitivesList.get(0));
        assertEquals((double) Float.MIN_VALUE, primitivesList.get(1));
        assertEquals((double) Float.MAX_VALUE, primitivesList.get(2));
    }

    @Test
    void testSetterWithDoubleArrayValue() {
        final YamlConfiguration cfg = new YamlConfiguration();

        assertNull(cfg.set("primitives.array", new double[] {2, Double.MIN_VALUE, Double.MAX_VALUE}));

        assertInstanceOf(List.class, cfg.get("primitives.array"));
        List<?> primitivesList = (List<?>) cfg.get("primitives.array");
        assert primitivesList != null;
        assertEquals(3, primitivesList.size());
        assertEquals((double) 2, primitivesList.get(0));
        assertEquals(Double.MIN_VALUE, primitivesList.get(1));
        assertEquals(Double.MAX_VALUE, primitivesList.get(2));
    }

    @Test
    void testSetterWithStringArrayValue() {
        final YamlConfiguration cfg = new YamlConfiguration();

        assertNull(cfg.set("primitives.array", new String[] {"zyx", "b", "a"}));

        assertInstanceOf(List.class, cfg.get("primitives.array"));
        List<?> primitivesList = (List<?>) cfg.get("primitives.array");
        assert primitivesList != null;
        assertEquals(3, primitivesList.size());
        assertEquals("zyx", primitivesList.get(0));
        assertEquals("b", primitivesList.get(1));
        assertEquals("a", primitivesList.get(2));
    }

    @Test
    void testHas() {
        final YamlConfiguration cfg = new YamlConfiguration();

        assertFalse(cfg.has(null));

        assertNull(cfg.set("foo", "bar"));

        assertTrue(cfg.has("foo"));
        assertFalse(cfg.has("bar"));

        assertNull(cfg.set("foo.bar", "baz"));
        assertTrue(cfg.has("foo.bar"));

        assertFalse(cfg.has("foo.baz"));
        assertNull(YamlConfiguration.getInnerMap(cfg.values, new String[] {"foo", "baz"}, false));
    }

    @Test
    void testReset() throws IOException {
        final YamlConfiguration cfg = new YamlConfiguration();
        cfg.load(new StringReader(inputYaml));

        cfg.setNodeComment("foo", "bar");
        cfg.setHeaderComment("baz");

        assertFalse(cfg.values.isEmpty());
        cfg.reset();
        assertTrue(cfg.values.isEmpty());

        assertNotNull(cfg.getHeaderComment());
        assertNotNull(cfg.getNodeComment("foo"));
    }

    @Test
    void testUnset() throws IOException {
        final YamlConfiguration cfg = new YamlConfiguration();
        cfg.load(new StringReader(inputYaml));

        Object unsetResult;

        assertTrue(cfg.has("foo"));
        unsetResult = cfg.unset("foo");
        assertEquals("bar", unsetResult);
        assertFalse(cfg.has("foo"));

        assertTrue(cfg.has("primitives"));
        assertTrue(cfg.has("primitives.int"));
        assertTrue(cfg.has("primitives.double"));
        unsetResult = cfg.unset("primitives.int");
        assertEquals(Integer.MIN_VALUE, unsetResult);
        assertFalse(cfg.has("primitives.int"));
        assertTrue(cfg.has("primitives.double"));

        unsetResult = cfg.unset("primitives");
        assertInstanceOf(Map.class, unsetResult);
        assertFalse(cfg.has("primitives"));
        assertFalse(cfg.has("primitives.double"));
        assertFalse(cfg.has("primitives.string"));

        unsetResult = cfg.unset("unknown.nested.key");
        assertNull(unsetResult);
        unsetResult = cfg.unset("unknown-key");
        assertNull(unsetResult);
    }

    @Test
    void testToString() throws IOException {
        final YamlConfiguration cfg = new YamlConfiguration();

        String firstToString = cfg.toString();

        assertTrue(firstToString.contains(YamlConfiguration.class.getSimpleName()));
        assertTrue(firstToString.contains(cfg.values.toString()));

        cfg.load(new StringReader(inputYaml));

        String secondToString = cfg.toString();

        assertNotEquals(firstToString, secondToString);
        assertTrue(secondToString.contains(YamlConfiguration.class.getSimpleName()));
        assertTrue(secondToString.contains(cfg.values.toString()));
    }

    @Test
    void testLoadWithInvalidYaml() {
        final YamlConfiguration cfg = new YamlConfiguration();

        IllegalStateException exception = assertThrowsExactly(IllegalStateException.class,
                () -> cfg.load(new StringReader("Hello world")));

        assertEquals("The YAML file does not have the expected tree structure: java.lang.String", exception.getMessage());
    }

    @Test
    void testHeaderComments() throws IOException {
        String expectedHeaderComment = "This is a header comment";

        YamlConfiguration cfg = new YamlConfiguration();
        cfg.setHeaderComment(expectedHeaderComment);
        cfg.set("foo", "bar");

        assertNotNull(cfg.getHeaderComment());
        assertEquals(expectedHeaderComment, cfg.getHeaderComment().get());

        assertEquals("# " + expectedHeaderComment + "\n\nfoo: bar\n", cfg.toYamlString());
    }

    @Test
    void testNodeComments() throws IOException {
        String expectedYaml = "# Foo-Comment\n" +
                "foo: bar\n" +
                "# Level1-Comment\n" +
                "level1:\n" +
                "  level2:\n" +
                "    # Level3-Comment\n" +
                "    level3: value\n";

        YamlConfiguration cfg = new YamlConfiguration();
        cfg.set("foo", "bar");
        cfg.set("level1.level2.level3", "value");

        cfg.setNodeComment("foo", "Foo-Comment");
        cfg.setNodeComment("level1", "Level1-Comment");
        cfg.setNodeComment("level1.level2.level3", "Level3-Comment");

        Supplier<String> currentNodeComment = cfg.getNodeComment("foo");
        assertNotNull(currentNodeComment);
        assertEquals("Foo-Comment", currentNodeComment.get());

        currentNodeComment = cfg.getNodeComment("level1");
        assertNotNull(currentNodeComment);
        assertEquals("Level1-Comment", currentNodeComment.get());

        currentNodeComment = cfg.getNodeComment("level1.level2");
        assertNull(currentNodeComment);

        currentNodeComment = cfg.getNodeComment("level1.level2.level3");
        assertNotNull(currentNodeComment);
        assertEquals("Level3-Comment", currentNodeComment.get());

        assertEquals(expectedYaml, cfg.toYamlString());
    }

    private enum TestEnum {
        ENUM_VALUE;

        @Override
        public String toString() {
            return "#toString(): " + super.toString();
        }
    }
}
