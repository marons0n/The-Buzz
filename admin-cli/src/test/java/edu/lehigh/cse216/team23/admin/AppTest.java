package edu.lehigh.cse216.team23.admin;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit test for the App class.
 */
public class AppTest {

    /**
     * Test case to check if the App runs successfully.
     */
    @Test
    public void testApp() {
        assertTrue(true);  // A basic test case, replace with meaningful logic as needed.
    }

    /**
     * Tests an input of 'T', which should return 'T' when prompted.
     */
    @Test
    public void testPromptValidInput() throws IOException {
        String input = "T\n";
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(input.getBytes()));
        BufferedReader reader = new BufferedReader(isr);

        char result = App.prompt(reader);
        assertEquals('T', result);
    }

    /**
     * Tests an invalid input (e.g. 'X'), and ensures it keeps prompting until a valid input is given.
     */
    @Test
    public void testPromptInvalidInput() throws IOException {
        String input = "X\nT\n";  // Invalid input followed by valid input
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(input.getBytes()));
        BufferedReader reader = new BufferedReader(isr);

        char result = App.prompt(reader);
        assertEquals('T', result);
    }

    /**
     * Tests the menu option '?' which should show the menu.
     */
    @Test
    public void testPromptHelpMenu() throws IOException {
        String input = "?\n";
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(input.getBytes()));
        BufferedReader reader = new BufferedReader(isr);

        char result = App.prompt(reader);
        assertEquals('?', result);
    }

    /**
     * Test for handling edge cases like an empty input.
     */
    @Test
    public void testPromptEmptyInput() throws IOException {
        String input = "\nT\n";  // Empty input first, then valid input
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(input.getBytes()));
        BufferedReader reader = new BufferedReader(isr);

        char result = App.prompt(reader);
        assertEquals('T', result);
    }
}
