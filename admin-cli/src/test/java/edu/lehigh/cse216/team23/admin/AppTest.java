package edu.lehigh.cse216.team23.admin;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class AppTest {
    private MockDatabase mockDb;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() {
        mockDb = new MockDatabase();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testShowAllIdeas() {
        App.showAllIdeas(mockDb);
        String expectedOutput = "All ideas:";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

    @Test
    public void testInsertNewIdea() {
        String input = "Test idea\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        App.insertNewIdea(mockDb);
        String expectedOutput = "Idea inserted successfully.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

    @Test
    public void testFindAllUsers() {
        App.findAllUsers(mockDb);
        String expectedOutput = "All users:";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

    @Test
    public void testQueryAllComments() {
        App.queryAllComments(mockDb);
        String expectedOutput = "Comment ID:";
        assertTrue(outContent.toString().contains(expectedOutput));
    }
}