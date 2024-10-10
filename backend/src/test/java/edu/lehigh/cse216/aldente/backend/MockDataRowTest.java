package edu.lehigh.cse216.aldente.backend;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for MockDataRow
 */
public class MockDataRowTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MockDataRowTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(MockDataRowTest.class);
    }

    /**
     * Ensure that the constructor populates every field of the object it
     * creates
     */
    public void testConstructor() {
        String title = "Test Title";
        String content = "Test Content";
        int id = 17;
        MockDataRow d = new MockDataRow(id, title, content, null);

        assertTrue(d.mTitle().equals(title));
        assertTrue(d.mContent().equals(content));
        assertTrue(d.mId() == id);
        assertFalse(d.mCreated() == null); // this can help catch subtle bugs!
    }

    /**
     * Ensure that the builder works correctly
     */
    public void testBuilder() {
        String title = "Test Title For Builder";
        String content = "Test Content For Builder";
        int id = 177;
        
        MockDataRow d = new MockDataRow(id, title, content, null);
        MockDataRow.Builder mdrBuilder = d.builder();
        // note that we could set values on the builder, which is not possible 
        // to do instances of record. For example we can do this: mdrBuilder.mId = -1
        // but cannot do this: d.mId = -1;
        MockDataRow d2 = mdrBuilder.build();

        assertTrue(d2.mId() == d.mId());
        assertTrue(d2.mTitle().equals(d.mTitle()));
        assertTrue(d2.mContent().equals(d.mContent()));
        assertTrue(d2.mCreated().equals(d.mCreated()));
    }
}