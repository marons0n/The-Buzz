package edu.lehigh.cse216.team23.backend;

import edu.lehigh.cse216.team23.backend.FileUploadService;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.http.staticfiles.StaticFileConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.UUID;
import java.util.Scanner;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.services.drive.Drive;
import com.google.gson.Gson;

public class App {

    // Hashtable to store sessionKey and userId
    public static Hashtable<String, String> userTable = new Hashtable<>();

    /** Default constructor for app, leave it empty */
    public App() {}

    /** If no port is passed in as an environment variable, it will be set to 8080 */
    public static final int DEFAULT_PORT_WEBSERVER = 8080;

    /**
    * Safely gets integer value from named env var if it exists, otherwise returns default
    * 
    * @envar      The name of the environment variable to get.
    * @defaultVal The integer value to use as the default if envar isn't found
    * 
    * @returns The best answer we could come up with for a value for envar
    */
    static int getIntFromEnv(String envar, int defaultVal) {
        if (envar == null || envar.length() == 0 || System.getenv(envar.trim()) == null) return defaultVal;
        try (Scanner sc = new Scanner(System.getenv(envar.trim()))) {
            if (sc.hasNextInt())
                return sc.nextInt();
            else
                System.err.printf("ERROR: Could not read %s from environment, using default of %d%n", envar, defaultVal);
        }
        return defaultVal;
    }

    /**
     * A simple webserver that connects to and uses a database.
     * Uses default port; customizes the logger.
     * 
     * Reads arguments from the environment and then uses those arguments to connect to the database.
     * Either DATABASE_URI should be set, or the values of POSTGRES_{IP, PORT, USER, PASS, DBNAME}.
     * @param args is an array that the user passes on the command line
     */
    public static void main(String[] args) throws IOException, GeneralSecurityException {

        // Set up Google Drive API
        Drive driveService = FileUploadService.getDriveService();
        
        if (driveService == null) {
            System.err.println("Failed to initialize Google Drive service.");
            return;
        }

        // Initialize FileUploadService with the Drive service
        FileUploadService fileUploadService = new FileUploadService(driveService);

        /* holds connection to the database created from environment variables */
        Database db = Database.getDatabase();

        Javalin app = Javalin.create(config -> {
            config.enableDevLogging();
        });

        if ("True".equalsIgnoreCase(System.getenv("CORS_ENABLED"))) {
            // print to console that I am enabling CORS
            System.out.println("Enabling CORS");
            // Enable CORS for the entire app
            final String acceptCrossOriginRequestsFrom = "http://localhost:3000"; 
            final String acceptedCrossOriginRoutes = "GET, POST, PUT, DELETE, OPTIONS";
            final String supportedRequestHeaders = "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin";
            // print to console the origin
            System.out.println("Origin: " + acceptCrossOriginRequestsFrom);
            enableCORS(app, acceptCrossOriginRequestsFrom, acceptedCrossOriginRoutes, supportedRequestHeaders);
        }

        // gson provides us a way to turn JSON into objects, and objects into JSON.
        final Gson gson = new Gson();

        // Configure routes
        Routes.configureRoutes(app, driveService, db, gson, fileUploadService);

        /**
         * Middleware to check for OAuth token
         *
         * This middleware checks for the presence of an OAuth token in the Authorization header.
         */
        app.before("/api/*", new OAuthMiddleware());
        app.before("/users/*", new OAuthMiddleware());
        app.before("/ideas", new OAuthMiddleware());
        app.before("/ideas/{id}", new OAuthMiddleware());
        app.before("/ideas/*", new OAuthMiddleware());

        // don't forget: nothing happens until we `start` the server
        app.start(getIntFromEnv("PORT", DEFAULT_PORT_WEBSERVER));
    }

    /**
     * Set up CORS headers for the OPTIONS verb, and for every response that the
     * server sends.  This only needs to be called once.
     * 
     * @param app the Javalin app on which to enable cors; create() already called on it
     * @param origin The server that is allowed to send requests to this server
     * @param methods The allowed HTTP verbs from the above origin
     * @param headers The headers that can be sent with a request from the above
     *                origin
     */
    private static void enableCORS(Javalin app, String origin, String methods, String headers) {
        System.out.println("!!! CAUTION: ~~~ ENABLING CORS ~~~ !!!");

        app.options("/*", ctx -> {
            String accessControlRequestHeaders = ctx.req.getHeader("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                ctx.res.setHeader("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = ctx.req.getHeader("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                ctx.res.setHeader("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            ctx.status(200);
        });

        app.before(ctx -> {
            ctx.res.setHeader("Access-Control-Allow-Origin", origin);
            ctx.res.setHeader("Access-Control-Allow-Methods", methods);
            ctx.res.setHeader("Access-Control-Allow-Headers", headers);
            ctx.res.setHeader("Access-Control-Allow-Credentials", "true"); // Allow credentials
        });
    }
}

