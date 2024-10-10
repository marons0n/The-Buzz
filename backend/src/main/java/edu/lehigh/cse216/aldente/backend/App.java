package edu.lehigh.cse216.aldente.backend;

// Import the Javalin package, so that we can make use of the "get" function to 
// create an HTTP GET route
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.util.Scanner;

// Import Google's JSON library
import com.google.gson.*;

/**
 * For now, our app creates an HTTP server that can only get and add data.
 * 
 * When an HTTP client connects to this server on the default Javalin port (8080),
 * and requests /hello, we return "Hello World".  Otherwise, we produce an error.
 */
public class App 
{
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
        if( envar == null || envar.length() == 0 || System.getenv( envar.trim() ) == null ) return defaultVal;
        try( Scanner sc = new Scanner( System.getenv( envar.trim() ) ) ){
            if( sc.hasNextInt() )
                return sc.nextInt();
            else
                System.err.printf( "ERROR: Could not read %s from environment, using default of %d%n", envar, defaultVal );
        }
        return defaultVal;
    }

    public static void main(String[] args) {
        // main_helloworld(args);
        // main_inMemory_datastore(args);
        main_uses_database(args);
    }

    public static void main_helloworld( String[] args ){
        // // the below line needs java 10 for `var`, and imo isn't very readable
        // var app = Javalin
        //     .create(/*config*/)
        //     .get("/hello", ctx -> ctx.result("Hello World"))
        //     .start(7070);
        //
        // // so we instead avoid use of `var` but otherwise do the same
        Javalin app = Javalin
                .create( 
                    config -> {
                        config.requestLogger.http( 
                            (ctx, ms) -> { 
                                System.out.printf( "%s%n", "=".repeat(42) );
                                System.out.printf( "%s\t%s\t%s%nfull url: %s%n", ctx.scheme(), ctx.method().name(), ctx.path(), ctx.fullUrl() );                                
                            } 
                        ); 
                    } 
                ).get( "/hello", ctx -> ctx.result("Hello World") )
                .start( /*default is 8080*/ );
    }

    public static void main_inMemory_datastore( String[] args ){
        // our javalin app on which most operations must be performed
        Javalin app = Javalin
                .create( 
                    config -> {
                        config.requestLogger.http( 
                            (ctx, ms) -> { 
                                System.out.printf( "%s%n", "=".repeat(42) );
                                System.out.printf( "%s\t%s\t%s%nfull url: %s%n", ctx.scheme(), ctx.method().name(), ctx.path(), ctx.fullUrl() );                                
                            } 
                        );

                        // set up static file serving. See: https://javalin.io/documentation#staticfileconfig
                        config.staticFiles.add(staticFiles -> {
                            staticFiles.hostedPath = "/";                   // change to host files on a subpath, like '/assets'
                            staticFiles.directory = "/public";              // the directory where your files are located
                            staticFiles.location = Location.CLASSPATH;      // Location.CLASSPATH (jar) or Location.EXTERNAL (file system)
                            staticFiles.precompress = false;                // if the files should be pre-compressed and cached in memory (optimization)
                            // staticFiles.aliasCheck = null;                  // you can configure this to enable symlinks (= ContextHandler.ApproveAliases())
                            // staticFiles.headers = Map.of(...);              // headers that will be set for the files
                            // staticFiles.skipFileFunction = req -> false;    // you can use this to skip certain files in the dir, based on the HttpServletRequest
                            // staticFiles.mimeTypes.add(mimeType, ext);       // you can add custom mimetypes for extensions
                        });

                        // set up static file serving. See: https://javalin.io/documentation#staticfileconfig
                        config.staticFiles.add(staticFiles -> {
                            staticFiles.hostedPath = "/";                   // change to host files on a subpath, like '/assets'
                            String static_location_override = System.getenv("STATIC_LOCATION");
                            if (static_location_override == null) { // serve from jar; files located in src/main/resources/public
                                staticFiles.directory = "/public";                  // the directory where your files are located
                                staticFiles.location = Location.CLASSPATH;          // Location.CLASSPATH (jar)
                            } else { // serve from filesystem
                                System.out.println( "Overriding location of static file serving using STATIC_LOCATION env var: " + static_location_override );
                                staticFiles.directory = static_location_override;   // the directory where your files are located
                                staticFiles.location = Location.EXTERNAL;           // Location.EXTERNAL (file system)
                            }
                            staticFiles.precompress = false;                   // if the files should be pre-compressed and cached in memory (optimization)
                            // staticFiles.aliasCheck = null;                  // you can configure this to enable symlinks (= ContextHandler.ApproveAliases())
                            // staticFiles.headers = Map.of(...);              // headers that will be set for the files
                            // staticFiles.skipFileFunction = req -> false;    // you can use this to skip certain files in the dir, based on the HttpServletRequest
                            // staticFiles.mimeTypes.add(mimeType, ext);       // you can add custom mimetypes for extensions
                        });
                    } 
                );
        

        // gson provides us a way to turn JSON into objects, and objects into JSON.
        //
        // NB: it must be final, so that it can be accessed from our lambdas
        //
        // NB: Gson is thread-safe.  See 
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();

        // dataStore holds all of the data that has been provided via HTTP requests
        //
        // NB: every time we shut down the server, we will lose all data, and 
        //     every time we start the server, we'll have an empty dataStore,
        //     with IDs starting over from 0.
        final MockDataStore dataStore = new MockDataStore();

        // GET route that returns all message titles and Ids.  All we do is get 
        // the data, embed it in a StructuredResponse, turn it into JSON, and 
        // return it.  If there's no data, we return "[]", so there's no need 
        // for error handling.
        app.get( "/messages", ctx -> {
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON
            StructuredResponse resp = new StructuredResponse( "ok" , null, dataStore.readAll() );
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        } );

        // GET route that returns everything for a single row in the MockDataStore.
        // The "{id}" suffix in the first parameter to get() becomes 
        // ctx.pathParam("id"), so that we can get the requested row ID.  If 
        // "{id}" isn't a number, Javalin will reply with a status 500 Internal
        // Server Error.  Otherwise, we have an integer, and the only possible 
        // error is that it doesn't correspond to a row with data.
        app.get( "/messages/{id}", ctx -> {
            // NB: the {} syntax "/messages/{id}" does not allow slashes ('/') as part of the parameter
            // NB: the <> syntax "/messages/<id>" allows slashes ('/') as part of the parameter
            int idx = Integer.parseInt( ctx.pathParam("id") );
            
            // NB: even on error, we return 200, but with a JSON object that describes the error.
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON
            
            MockDataRow data = dataStore.readOne(idx);
            StructuredResponse resp = null;
            if (data == null) { // row not found, so return an error response
                resp = new StructuredResponse("error", "Data with row id " + idx + " not found", null);
            } else { // we found it, so just return the data
                resp = new StructuredResponse("ok", null, data);
            }
            
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        } );

        // POST route for adding a new element to the MockDataStore.  This will read
        // JSON from the body of the request, turn it into a SimpleRequest 
        // object, extract the title and message, insert them, and return the 
        // ID of the newly created row.
        app.post("/messages", ctx -> {
            // NB: even on error, we return 200, but with a JSON object that describes the error.
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON
            StructuredResponse resp = null;

            // get the request json from the ctx body, turn it into SimpleRequest instance
            // NB: if gson.Json fails, expect server reply with status 500 Internal Server Error
            SimpleRequest req = gson.fromJson(ctx.body(), SimpleRequest.class);
            
            // NB: add to MockDataStore; createEntry checks for null title and message
            int newId = dataStore.createEntry(req.mTitle(), req.mMessage());
            if (newId == -1) {
                resp = new StructuredResponse("error", "error performing insertion (title or message null?)", null);
            } else {
                resp = new StructuredResponse("ok", Integer.toString(newId), null);
            }
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        });

        app.put("/messages/{id}", ctx -> {
            int idx = Integer.parseInt(ctx.pathParam("id"));

            ctx.status( 200 );
            ctx.contentType("application/json");
            StructuredResponse resp = null;

            SimpleRequest req = gson.fromJson(ctx.body(), SimpleRequest.class);

            MockDataRow result = dataStore.updateOne(idx, req.mTitle(), req.mMessage());
            if(result == null) {
                resp = new StructuredResponse("error", "unable to update row " + idx, null);
            } else {
                resp = new StructuredResponse("ok", null, result);
            }
            ctx.result(gson.toJson(resp));
        });

        app.delete("messages/{id}", ctx -> {
            int idx = Integer.parseInt(ctx.pathParam("id"));

            ctx.status(200);
            ctx.contentType("application/json");
            StructuredResponse resp = null;

            boolean result = dataStore.deleteOne(idx);
            if (!result) {
                resp = new StructuredResponse("error", "unable to delete row " + idx, null);
            } else {
                resp = new StructuredResponse("ok", null, null);
            }
            ctx.result( gson.toJson( resp ) );
        });

        // don't forget: nothing happens until we `start` the server
        app.start( /*default is 8080*/ );
    }

    /**
     * A simple webserver that connects to and uses a database.
     * Uses default port; customizes the logger.
     * 
     * Reads arguments from the environment and then uses those arguments to connect to the database.
     * Either DATABASE_URI should be set, or the values of POSTGRES_{IP, PORT, USER, PASS, DBNAME}.
     * 
     * Features yet to be implemented:
     * Supports GET to /messages to retrieve all messages (without their body)
     * Supports POST to /messages to create a new message
     * Supports {GET, PUT, DELETE} to /messages/{id} to do associated action on message with given id
     */
    public static void main_uses_database( String[] args){
        /* holds connection to the database created from environment variables */
        Database db = Database.getDatabase();

        Javalin app = Javalin
                .create( 
                    config -> {
                        config.requestLogger.http( 
                            (ctx, ms) -> { 
                                System.out.printf( "%s%n", "=".repeat(42) );
                                System.out.printf( "%s\t%s\t%s%nfull url: %s%n", ctx.scheme(), ctx.method().name(), ctx.path(), ctx.fullUrl() );                                
                            } 
                        );

                        // set up static file serving. See: https://javalin.io/documentation#staticfileconfig
                        config.staticFiles.add(staticFiles -> {
                            staticFiles.hostedPath = "/";                   // change to host files on a subpath, like '/assets'
                            staticFiles.directory = "/public";              // the directory where your files are located
                            staticFiles.location = Location.CLASSPATH;      // Location.CLASSPATH (jar) or Location.EXTERNAL (file system)
                            staticFiles.precompress = false;                // if the files should be pre-compressed and cached in memory (optimization)
                            // staticFiles.aliasCheck = null;                  // you can configure this to enable symlinks (= ContextHandler.ApproveAliases())
                            // staticFiles.headers = Map.of(...);              // headers that will be set for the files
                            // staticFiles.skipFileFunction = req -> false;    // you can use this to skip certain files in the dir, based on the HttpServletRequest
                            // staticFiles.mimeTypes.add(mimeType, ext);       // you can add custom mimetypes for extensions
                        });

                        // set up static file serving. See: https://javalin.io/documentation#staticfileconfig
                        config.staticFiles.add(staticFiles -> {
                            staticFiles.hostedPath = "/";                   // change to host files on a subpath, like '/assets'
                            String static_location_override = System.getenv("STATIC_LOCATION");
                            if (static_location_override == null) { // serve from jar; files located in src/main/resources/public
                                staticFiles.directory = "/public";                  // the directory where your files are located
                                staticFiles.location = Location.CLASSPATH;          // Location.CLASSPATH (jar)
                            } else { // serve from filesystem
                                System.out.println( "Overriding location of static file serving using STATIC_LOCATION env var: " + static_location_override );
                                staticFiles.directory = static_location_override;   // the directory where your files are located
                                staticFiles.location = Location.EXTERNAL;           // Location.EXTERNAL (file system)
                            }
                            staticFiles.precompress = false;                   // if the files should be pre-compressed and cached in memory (optimization)
                            // staticFiles.aliasCheck = null;                  // you can configure this to enable symlinks (= ContextHandler.ApproveAliases())
                            // staticFiles.headers = Map.of(...);              // headers that will be set for the files
                            // staticFiles.skipFileFunction = req -> false;    // you can use this to skip certain files in the dir, based on the HttpServletRequest
                            // staticFiles.mimeTypes.add(mimeType, ext);       // you can add custom mimetypes for extensions
                        });
                    } 
                );

        if ("True".equalsIgnoreCase(System.getenv("CORS_ENABLED"))) {
            final String acceptCrossOriginRequestsFrom = "*";
            final String acceptedCrossOriginRoutes = "GET,PUT,POST,DELETE,OPTIONS";
            final String supportedRequestHeaders = "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin";
            enableCORS(app, acceptCrossOriginRequestsFrom, acceptedCrossOriginRoutes, supportedRequestHeaders);
        }
        
        app.get( "/helloworld", ctx -> { 
            ctx.status( 200 ); // status 200 OK
            ctx.result("Hello World from my tutorial app!");
        } );

        // gson provides us a way to turn JSON into objects, and objects into JSON.
        //
        // NB: it must be final, so that it can be accessed from our lambdas
        //
        // NB: Gson is thread-safe.  See 
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();

        // GET route that returns all message titles and Ids.  All we do is get 
        // the data, embed it in a StructuredResponse, turn it into JSON, and 
        // return it.  If there's no data, we return "[]", so there's no need 
        // for error handling.
        app.get( "/messages", ctx -> {
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON
            StructuredResponse resp = new StructuredResponse( "ok" , null, db.selectAll() );
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        } );

        // GET route that returns everything for a single row in the MockDataStore.
        // The "{id}" suffix in the first parameter to get() becomes 
        // ctx.pathParam("id"), so that we can get the requested row ID.  If 
        // "{id}" isn't a number, Javalin will reply with a status 500 Internal
        // Server Error.  Otherwise, we have an integer, and the only possible 
        // error is that it doesn't correspond to a row with data.
        app.get( "/messages/{id}", ctx -> {
            // NB: the {} syntax "/messages/{id}" does not allow slashes ('/') as part of the parameter
            // NB: the <> syntax "/messages/<id>" allows slashes ('/') as part of the parameter
            int idx = Integer.parseInt( ctx.pathParam("id") );
            
            // NB: even on error, we return 200, but with a JSON object that describes the error.
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON
            
            Database.RowData data = db.selectOne(idx);
            StructuredResponse resp = null;
            if (data == null) { // row not found, so return an error response
                resp = new StructuredResponse("error", "Data with row id " + idx + " not found", null);
            } else { // we found it, so just return the data
                resp = new StructuredResponse("ok", null, data);
            }
            
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        } );

        // POST route for adding a new element to the MockDataStore.  This will read
        // JSON from the body of the request, turn it into a SimpleRequest 
        // object, extract the title and message, insert them, and return the 
        // ID of the newly created row.
        app.post("/messages", ctx -> {
            // NB: even on error, we return 200, but with a JSON object that describes the error.
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON
            StructuredResponse resp = null;

            // get the request json from the ctx body, turn it into SimpleRequest instance
            // NB: if gson.Json fails, expect server reply with status 500 Internal Server Error
            SimpleRequest req = gson.fromJson(ctx.body(), SimpleRequest.class);
            
            // NB: add to MockDataStore; createEntry checks for null title and message
            int newId = db.insertRow(req.mTitle(), req.mMessage());
            if (newId == -1) {
                resp = new StructuredResponse("error", "error performing insertion (title or message null?)", null);
            } else {
                resp = new StructuredResponse("ok", Integer.toString(newId), null);
            }
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        });

        app.put("/messages/{id}", ctx -> {
            int idx = Integer.parseInt(ctx.pathParam("id"));

            ctx.status( 200 );
            ctx.contentType("application/json");
            StructuredResponse resp = null;

            SimpleRequest req = gson.fromJson(ctx.body(), SimpleRequest.class);

            int result = db.updateOne(idx, req.mTitle(), req.mMessage());
            if(result == -1) {
                resp = new StructuredResponse("error", "unable to update row " + idx, null);
            } else {
                resp = new StructuredResponse("ok", null, "updated row " + idx);
            }
            ctx.result(gson.toJson(resp));
        });

        app.delete("messages/{id}", ctx -> {
            int idx = Integer.parseInt(ctx.pathParam("id"));

            ctx.status(200);
            ctx.contentType("application/json");
            StructuredResponse resp = null;

            int result = db.deleteRow(idx);
            if (result == -1) {
                resp = new StructuredResponse("error", "unable to delete row " + idx, null);
            } else {
                resp = new StructuredResponse("ok", null, null);
            }
            ctx.result( gson.toJson( resp ) );
        });

        app.post("like/{id}", ctx -> {
            int idx = Integer.parseInt(ctx.pathParam("id"));

            ctx.status(200);
            ctx.contentType("application/json");
            StructuredResponse resp = null;

            int result = db.deleteRow(idx);
            if (result == -1) {
                resp = new StructuredResponse("error", "unable to delete row " + idx, null);
            } else {
                resp = new StructuredResponse("ok", null, null);
            }
            ctx.result( gson.toJson( resp ) );
        });

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
    app.options( "/*", ctx -> {
        String accessControlRequestHeaders = ctx.req().getHeader( "Access-Control-Request-Headers");
        if (accessControlRequestHeaders != null) {
            ctx.res().setHeader("Access-Control-Allow-Headers", accessControlRequestHeaders);
        }
        
        String accessControlRequestMethod = ctx.req().getHeader("Access-Control-Request-Method");
        if (accessControlRequestMethod != null) {
            ctx.res().setHeader("Access-Control-Allow-Methods", accessControlRequestMethod);
        }
    });

    // 'before' is a decorator, which will run before any get/post/put/delete.
    // In our case, it will put three extra CORS headers into the response
    app.before( handler -> {
        handler.header("Access-Control-Allow-Origin", origin);
        handler.header("Access-Control-Request-Method", methods);
        handler.header("Access-Control-Allow-Headers", headers);
    });
}
}