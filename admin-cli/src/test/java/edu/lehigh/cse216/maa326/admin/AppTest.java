package edu.lehigh.cse216.maa326.admin;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    /**
     * Tests an input of 'T', which should return true and create a table
     */
    public void testPromptValidInput() throws IOException {
        String input = "T\n";
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(input.getBytes()));
        BufferedReader reader = new BufferedReader(isr);

        char result = App.prompt(reader);
        assertEquals('T', result);
    }

}
