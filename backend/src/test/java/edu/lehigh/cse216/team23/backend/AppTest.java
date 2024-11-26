package edu.lehigh.cse216.team23.backend;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import java.util.Hashtable;

public class AppTest {

    private static Database mockDatabase;
    private static Hashtable<String, String> mockSessionCache;

    @BeforeAll
    static void setup() {
        // Mock the database and session cache
        mockDatabase = mock(Database.class);
        mockSessionCache = new Hashtable<>();
    }

    @Test
    void testFileUploadAndRetrieval() {
        Javalin app = Javalin.create(config -> config.defaultContentType = "application/json");

        // Mock database behavior for file upload
        when(mockDatabase.insertIdeaWithFileLink(anyString(), anyString(), anyString()))
                .thenReturn(1); // Simulate successful insertion with ID = 1

        app.post("/upload", ctx -> {
            String userId = ctx.formParam("userId");
            String message = ctx.formParam("message");
            String fileLink = ctx.formParam("fileLink");

            int newId = mockDatabase.insertIdeaWithFileLink(userId, message, fileLink);
            if (newId == -1) {
                ctx.status(500).json(new StructuredResponse("error", "File upload failed", null));
            } else {
                ctx.status(200).json(new StructuredResponse("ok", "File uploaded", newId));
            }
        });

        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/upload")
                    .body("{\"userId\": \"user123\", \"message\": \"Test message\", \"fileLink\": \"http://example.com/file\"}")
                    .asString();

            assertEquals(200, response.getStatus());
            assertTrue(response.getBody().contains("File uploaded"));
        });
    }

    @Test
    void testSessionIdCaching() {
        String sessionId = "session123";
        String userId = "user123";

        // Simulate caching the session ID
        mockSessionCache.put(sessionId, userId);

        // Retrieve and verify session ID
        assertEquals(userId, mockSessionCache.get(sessionId));

        // Simulate logout and removal from cache
        mockSessionCache.remove(sessionId);
        assertNull(mockSessionCache.get(sessionId));
    }

    @Test
    void testFileDownloadWithCache() {
        Javalin app = Javalin.create(config -> config.defaultContentType = "application/json");
        Hashtable<String, String> fileCache = new Hashtable<>();

        // Mock database behavior for file retrieval
        when(mockDatabase.selectOne(anyInt())).thenReturn(
                new Database.RowDataIdeas(1, 0, "Test message", "http://example.com/file"));

        app.get("/files/:id", ctx -> {
            int fileId = Integer.parseInt(ctx.pathParam("id"));
            if (fileCache.containsKey(String.valueOf(fileId))) {
                ctx.status(200).json(new StructuredResponse("ok", "File retrieved from cache",
                        fileCache.get(String.valueOf(fileId))));
            } else {
                Database.RowDataIdeas data = mockDatabase.selectOne(fileId);
                if (data != null) {
                    fileCache.put(String.valueOf(fileId), data.mMessage());
                    ctx.status(200)
                            .json(new StructuredResponse("ok", "File retrieved from DB and cached", data.mMessage()));
                } else {
                    ctx.status(404).json(new StructuredResponse("error", "File not found", null));
                }
            }
        });

        JavalinTest.test(app, (server, client) -> {
            // First call - simulate retrieving from database
            var response1 = client.get("/files/1").asString();
            assertEquals(200, response1.getStatus());
            assertTrue(response1.getBody().contains("File retrieved from DB and cached"));

            // Second call - simulate retrieving from cache
            var response2 = client.get("/files/1").asString();
            assertEquals(200, response2.getStatus());
            assertTrue(response2.getBody().contains("File retrieved from cache"));
        });
    }
}
