package edu.lehigh.cse216.team23.backend;

import io.javalin.Javalin;
import io.javalin.http.UploadedFile;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.lehigh.cse216.team23.backend.Database;
import edu.lehigh.cse216.team23.backend.StructuredResponse;
import edu.lehigh.cse216.team23.backend.SimpleRequest;
import edu.lehigh.cse216.team23.backend.SimpleRequestComments;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.UUID;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

public class Routes {

    // Hashtable to store sessionKey and userId
    public static Hashtable<String, String> userTable = new Hashtable<>();

    public static void configureRoutes(Javalin app, Drive driveService, Database db, Gson gson, FileUploadService fileUploadService) {

        //IDEAS
        // Get all ideas
        app.get("/ideas", ctx -> {
            System.out.println("Getting all ideas");
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON
            StructuredResponse resp = new StructuredResponse("ok", null, db.selectAll());
            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });

        //gets 1 individual idea
        app.get( "/ideas/{id}", ctx -> {
            int idx = Integer.parseInt( ctx.pathParam("id") );
            
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON
            
            Database.RowDataIdeas data = db.selectOne(idx);
            StructuredResponse resp = null;
            if (data == null) { // row not found, so return an error response
                resp = new StructuredResponse("error", "Data with row id " + idx + " not found", null);
            } else { // we found it, so just return the data
                resp = new StructuredResponse("ok", null, data);
            }
            
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        } );


        //posts a new idea
        app.post("/ideas", ctx -> {
            // Configure multipart handling
            ctx.req.setAttribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            // Get the message parameter
            String message = ctx.formParam("mMessage");

            // Get the userId attribute
            String userId = ctx.attribute("userId");
            String googleDriveFile = null;
            String link = ctx.formParam("mLink");

            // Get the uploaded file
            Part filePart = null;
            try {
                filePart = ctx.req.getPart("mFile");
                System.out.println("File part: " + filePart);
            } catch (Exception e) {
                // No file was uploaded
                System.out.println("No file was uploaded");
            }

            Path tempFile = null;
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                tempFile = Files.createTempFile(fileName, "");
                // print to console the file name
                System.out.println("File name: " + fileName);

                // Save the file to a temporary location
                try (InputStream input = filePart.getInputStream()) {
                    Files.copy(input, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    // print to console the file path
                    System.out.println("File path: " + tempFile);
                } catch (Exception e) {
                    // Log the error to the console
                    System.out.println("Failed to save file to temporary location: " + e.getMessage());
                    ctx.status(500).result("Failed to save file to temporary location");
                    return;
                }
            }

            try {
                if (tempFile != null) {
                    System.out.println("Attempting to upload file to Google Drive");
                    // Upload the file to Google Drive
                    String folderId = "1B7Dt95_72lGcfcpDRfyaM_kaX_BYYj7q"; // Folder ID for BuzzData
                    String fileId = fileUploadService.uploadFile(tempFile.toFile(), filePart.getSubmittedFileName(), "application/octet-stream", folderId);
                    // print to console the file id
                    System.out.println("File ID: " + fileId);

                    // Create a JSON response
                    JsonObject jsonResponse = new JsonObject();
                    jsonResponse.addProperty("message", "File uploaded to Drive successfully");
                    jsonResponse.addProperty("fileId", fileId);

                    ctx.result(jsonResponse.toString());
                    ctx.contentType("application/json");
                    // print to console that the file was successfully uploaded
                    System.out.println("File uploaded to Drive successfully with ID: " + fileId);
                    // get the full path to the google drive file
                    googleDriveFile = "https://drive.google.com/file/d/" + fileId + "/view";
                    // print to console the full path to the google drive file
                    System.out.println("Google Drive file: " + googleDriveFile);
                } else {
                    // Handle the case where no file is uploaded
                    JsonObject jsonResponse = new JsonObject();
                    jsonResponse.addProperty("message", "Idea created without file");

                    ctx.result(jsonResponse.toString());
                    ctx.contentType("application/json");
                    System.out.println("Idea created without file");
                }
            } catch (Exception e) {
                // Log the error to the console
                System.out.println("Failed to upload file to Google Drive: " + e.getMessage());
                ctx.status(500).result("Failed to upload file to Google Drive");
            } finally {
                if (tempFile != null) {
                    try {
                        // Clean up temporary file
                        Files.delete(tempFile);
                        // print to console that the temporary file was deleted
                        System.out.println("Temporary file deleted");
                    } catch (Exception e) {
                        // Log the error to the console
                        System.err.println("Failed to delete temporary file: " + e.getMessage());
                    }
                }
            }

            // Insert the new idea into the database
            int newId = db.insertRow(userId, message, googleDriveFile, link);
            if (newId == -1) {
                ctx.status(500).json(new StructuredResponse("error", "Failed to insert idea", null));
            } else {
                ctx.status(200).json(new StructuredResponse("ok", Integer.toString(newId), null));
            }
        });

//COMMENTS

        //get comments
        app.get("/ideas/{id}/comments", ctx -> {
            int postId = Integer.parseInt(ctx.pathParam("id"));
            System.out.println("Getting comments for post with id: " + postId);
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON
            StructuredResponse resp = new StructuredResponse("ok", null, db.selectAllComments(postId));
            System.out.println(gson.toJson(resp));
            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });

        //add comment to idea
        app.post("/ideas/{id}/comments", ctx -> {
            // Configure multipart handling
            ctx.req.setAttribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

            // Get the message parameter
            String message = ctx.formParam("mComment");

            // Get the userId attribute
            String userId = ctx.attribute("userId");
            String googleDriveFile = null;
            String link = ctx.formParam("mLink");
            int postId = Integer.parseInt(ctx.pathParam("id"));

            // Get the uploaded file
            Part filePart = null;
            try {
                filePart = ctx.req.getPart("mFile");
                System.out.println("File part: " + filePart);
            } catch (Exception e) {
                // No file was uploaded
                System.out.println("No file was uploaded");
            }

            Path tempFile = null;
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                tempFile = Files.createTempFile(fileName, "");
                // print to console the file name
                System.out.println("File name: " + fileName);

                // Save the file to a temporary location
                try (InputStream input = filePart.getInputStream()) {
                    Files.copy(input, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    // print to console the file path
                    System.out.println("File path: " + tempFile);
                } catch (Exception e) {
                    // Log the error to the console
                    System.out.println("Failed to save file to temporary location: " + e.getMessage());
                    ctx.status(500).result("Failed to save file to temporary location");
                    return;
                }
            }

            try {
                if (tempFile != null) {
                    System.out.println("Attempting to upload file to Google Drive");
                    // Upload the file to Google Drive
                    String folderId = "1B7Dt95_72lGcfcpDRfyaM_kaX_BYYj7q"; // Folder ID for BuzzData
                    String fileId = fileUploadService.uploadFile(tempFile.toFile(), filePart.getSubmittedFileName(), "application/octet-stream", folderId);
                    // print to console the file id
                    System.out.println("File ID: " + fileId);

                    // Create a JSON response
                    JsonObject jsonResponse = new JsonObject();
                    jsonResponse.addProperty("comment", "File uploaded to Drive successfully");
                    jsonResponse.addProperty("fileId", fileId);

                    ctx.result(jsonResponse.toString());
                    ctx.contentType("application/json");
                    // print to console that the file was successfully uploaded
                    System.out.println("File uploaded to Drive successfully with ID: " + fileId);
                    // get the full path to the google drive file
                    googleDriveFile = "https://drive.google.com/file/d/" + fileId + "/view";
                    // print to console the full path to the google drive file
                    System.out.println("Google Drive file: " + googleDriveFile);
                } else {
                    // Handle the case where no file is uploaded
                    JsonObject jsonResponse = new JsonObject();
                    jsonResponse.addProperty("comment", "Comment created without file");

                    ctx.result(jsonResponse.toString());
                    ctx.contentType("application/json");
                    System.out.println("Comment created without file");
                }
            } catch (Exception e) {
                // Log the error to the console
                System.out.println("Failed to upload file to Google Drive: " + e.getMessage());
                ctx.status(500).result("Failed to upload file to Google Drive");
            } finally {
                if (tempFile != null) {
                    try {
                        // Clean up temporary file
                        Files.delete(tempFile);
                        // print to console that the temporary file was deleted
                        System.out.println("Temporary file deleted");
                    } catch (Exception e) {
                        // Log the error to the console
                        System.err.println("Failed to delete temporary file: " + e.getMessage());
                    }
                }
            }

            // Insert the new idea into the database
            int newId = db.insertComment(userId, postId, message, googleDriveFile, link);
            if (newId == -1) {
                ctx.status(500).json(new StructuredResponse("error", "Failed to insert comment", null));
            } else {
                ctx.status(200).json(new StructuredResponse("ok", Integer.toString(newId), null));
            }
        });

//VOTES
        //Upvote an idea
        app.put("/ideas/{id}/upvote", ctx -> {
            // print to console that the like request was received and what the id is
            System.out.println("Like request received for idea with id: " + ctx.pathParam("id"));

            int postId = Integer.parseInt(ctx.pathParam("id"));
            String userId = ctx.attribute("userId");
    

            if (userId == null) {
                // print to console that the user data is invalid and include id
                System.out.println("Invalid user data: " + userId);
                ctx.status(400).json(new StructuredResponse("error", "Invalid user data", null));
                return;
            }

            // print to console post id and user id
            System.out.println("Upvoting using Post ID: " + postId);
            System.out.println("Upvoting User ID: " + userId);

            int voteId = db.insertVote(userId, postId, 1);
            //update votes in database
            db.upVote(postId);

            StructuredResponse resp;
            if (voteId == -1) {
                resp = new StructuredResponse("error", "error performing insertion (vote null?)", null);
            } else {
                resp = new StructuredResponse("ok", Integer.toString(voteId), null);
            }
            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });

        //Downvote a post
        app.put("/ideas/{id}/downvote", ctx -> {
            int postId = Integer.parseInt(ctx.pathParam("id"));
            String userId = ctx.attribute("userId");
            if (userId == null) {
                ctx.status(400).json(new StructuredResponse("error", "Invalid user data", null));
                return;
            }

            int voteId = db.insertVote(userId, postId, -1);
            db.downVote(postId);
            StructuredResponse resp;
            if (voteId == -1) {
                resp = new StructuredResponse("error", "error performing insertion (vote null?)", null);
            } else {
                resp = new StructuredResponse("ok", Integer.toString(voteId), null);
            }
            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });

//USERS
        // Add user to database
        app.post("/users", ctx -> {
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON
            StructuredResponse resp = null;

            //cast the attribute to an int
            String userId = ctx.attribute("userId");
            String email = ctx.attribute("email");
            String name = ctx.attribute("name");

            if (email == null || name == null) {
                resp = new StructuredResponse("error", "Invalid user data", null);
                ctx.result(gson.toJson(resp)); // return JSON representation of response
                return;
            }

            System.out.println("Inserting new user");
            //print userid
            System.out.println("User ID: " + userId);

            //chech if user already exists
            if (db.emailExists(userId)) {
                resp = new StructuredResponse("error", "User already exists", null);
                ctx.result(gson.toJson(resp)); // return JSON representation of response
                return;
            } else {
                int newUserId = db.insertUser(userId, name, email);
                if (newUserId == -1) {
                    resp = new StructuredResponse("error", "error performing insertion (user null?)", null);
                } else {
                    resp = new StructuredResponse("ok", Integer.toString(newUserId), null);
                }
                }
                ctx.result(gson.toJson(resp)); // return JSON representation of response
        });

        // Get all users
        app.get( "/users", ctx -> {
            System.out.println("Getting all users");
            ctx.status( 200 ); // status 200 OK
            ctx.contentType( "application/json" ); // MIME type of JSON
            StructuredResponse resp = new StructuredResponse( "ok" , null, db.selectAllUsers() );
            ctx.result( gson.toJson( resp ) ); // return JSON representation of response
        } );

        // update user bio
        app.put("/users/{id}/bio", ctx -> {
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON
            StructuredResponse resp = null;
            String userId = ctx.attribute("userId");
            if (userId == null) {
                resp = new StructuredResponse("error", "Invalid user data", null);
                ctx.result(gson.toJson(resp)); // return JSON representation of response
                return;
            }
             // Extract the message from the request body
             SimpleRequestUserBio req = gson.fromJson(ctx.body(), SimpleRequestUserBio.class);
             if (req == null || req.mBio() == null) {
                 resp = new StructuredResponse("error", "Invalid message", null);
                 ctx.result(gson.toJson(resp)); // return JSON representation of response
                 return;
             }
            // Insert the comment into the database
            int userBio = db.updateUserBio(userId, req.mBio());
            if (userBio == -1) {
                resp = new StructuredResponse("error", "error performing insertion (message null?)", null);
            } else {
                resp = new StructuredResponse("ok", Integer.toString(userBio), null);
            }
            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });

         // update user gender identity
         app.put("/users/{id}/gender", ctx -> {
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON
            StructuredResponse resp = null;
            String userId = ctx.attribute("userId");
            if (userId == null) {
                resp = new StructuredResponse("error", "Invalid user data", null);
                ctx.result(gson.toJson(resp)); // return JSON representation of response
                return;
            }
             // Extract the message from the request body
             SimpleRequestUserGender req = gson.fromJson(ctx.body(), SimpleRequestUserGender.class);
             if (req == null || req.mGender() == null) {
                 resp = new StructuredResponse("error", "Invalid message", null);
                 ctx.result(gson.toJson(resp)); // return JSON representation of response
                 return;
             }
            // Insert the comment into the database
            int userGender = db.updateUserGender(userId, req.mGender());
            if (userGender == -1) {
                resp = new StructuredResponse("error", "error performing insertion (message null?)", null);
            } else {
                resp = new StructuredResponse("ok", Integer.toString(userGender), null);
            }
            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });

        // update user sexual orientation
        app.put("/users/{id}/orientation", ctx -> {
            ctx.status(200); // status 200 OK
            ctx.contentType("application/json"); // MIME type of JSON
            StructuredResponse resp = null;
            String userId = ctx.attribute("userId");
            if (userId == null) {
                resp = new StructuredResponse("error", "Invalid user data", null);
                ctx.result(gson.toJson(resp)); // return JSON representation of response
                return;
            }
             // Extract the message from the request body
             SimpleRequestUserOrientation req = gson.fromJson(ctx.body(), SimpleRequestUserOrientation.class);
             if (req == null || req.mOrientation() == null) {
                 resp = new StructuredResponse("error", "Invalid message", null);
                 ctx.result(gson.toJson(resp)); // return JSON representation of response
                 return;
             }
            // Insert the comment into the database
            int userOrientation = db.updateUserGender(userId, req.mOrientation());
            if (userOrientation == -1) {
                resp = new StructuredResponse("error", "error performing insertion (message null?)", null);
            } else {
                resp = new StructuredResponse("ok", Integer.toString(userOrientation), null);
            }
            ctx.result(gson.toJson(resp)); // return JSON representation of response
        });


//OAUTH
        // Endpoint to handle OAuth 2.0 callback
        app.get("/oauth2callback", ctx -> {
            String code = ctx.queryParam("code");
            String state = ctx.queryParam("state");
            //print state
            System.out.println("State: " + state);

            state = state.replace("/login", "");

            if (code == null) {
                ctx.result("Missing authorization code");
                return;
            }
        
            try {
                TokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                        new NetHttpTransport(),
                        GsonFactory.getDefaultInstance(),
                        "https://oauth2.googleapis.com/token",
                        OAuthConfig.getClientId(),
                        OAuthConfig.getClientSecret(),
                        code,
                        OAuthConfig.getRedirectUri() // Specify the same redirect URI that you use to get the authorization code
                ).execute();
        
                // Verify token, error if not valid
                String idTokenString = tokenResponse.get("id_token").toString();
        
                GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                        .setAudience(Collections.singletonList(OAuthConfig.getClientId()))
                        .build();
        
                GoogleIdToken idToken = verifier.verify(idTokenString);
                if (idToken == null) {
                    ctx.result("Invalid ID token.");
                    return;
                }
        
                GoogleIdToken.Payload payload = idToken.getPayload();
                String userId = payload.getSubject();  // User's unique ID
                String email = payload.getEmail();
                String name = (String) payload.get("name");
        
                if (db != null) {
                    // Check if user already exists
                    if (db.emailExists(email)) {
                        // User already exists, create a new session key
                        String sessionKey = UUID.randomUUID().toString();
                        userTable.put(sessionKey, userId);
                        System.out.println("Session Key for existing user: " + sessionKey);
                        ctx.cookie("session_key", sessionKey, 3600); // Expires in 1 hour
                        ctx.cookie("auth_token", idTokenString, 3600); // Expires in 1 hour
                        ctx.redirect(state);
                        //print session key

                        return;
                    } else {
                        int result = db.insertUser(userId, name, email);
                        if (result == -1) {
                            System.err.println("Error inserting user into the database");
                        } else if (result == 0) {
                            System.out.println("User already exists in the database");
                        } else {
                            System.out.println("User successfully inserted into the database");
                        }
                    }
        
                    // Generate a session key for the user (new or existing)
                    String sessionKey = UUID.randomUUID().toString();
                    userTable.put(sessionKey, userId);
                    ctx.cookie("session_key", sessionKey, 3600); // Expires in 1 hour
                    ctx.cookie("auth_token", idTokenString, 3600); // Expires in 1 hour
                    
                    // Redirect back to the originating page based on   he state parameter
                    if (state != null && !state.isEmpty()) {
                        ctx.redirect(state);
                    } else {
                        ctx.redirect("/");
                    }
                    
                } else {
                    System.err.println("Database connection is null");
                }
            } catch (Exception e) {
                ctx.result("Error during OAuth callback: " + e.getMessage());
                e.printStackTrace();
            }
        });

        // Define your routes
        app.get("/api/protected-endpoint", ctx -> {
            String userId = ctx.attribute("userId");
            if (userId == null) {
                ctx.status(400).json(new StructuredResponse("error", "Invalid user data", null));
                return;
            }
            ctx.status(200).json(new StructuredResponse("ok", "You have access as " + userId, null));
        });

        // Login endpoint
        app.post("/login", ctx -> {
            // Parse the request body to get the ID token
            String idTokenString = ctx.body();
            if (idTokenString == null || idTokenString.isEmpty()) {
                ctx.status(400).result("Missing ID token");
                return;
            }

            // Verify the ID token
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(OAuthConfig.getClientId())) // Replace with your client ID
                    .build();

            GoogleIdToken idToken;
            try {
                idToken = verifier.verify(idTokenString);
                if (idToken == null) {
                    ctx.status(401).result("Invalid ID token");
                    return;
                }
            } catch (GeneralSecurityException | IOException e) {
                ctx.status(401).result("Invalid ID token");
                return;
            }

            // Extract user information from the ID token
            GoogleIdToken.Payload payload = idToken.getPayload();
            String userId = payload.getSubject();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            // Respond with user information
            ctx.status(200).json(new StructuredResponse("ok", "Login successful", new SimpleRequestUser(userId, email, name)));
        });

        // Example endpoint to get user information using session key
        app.get("/user", ctx -> {
            String sessionKey = ctx.cookie("session_key");
            if (sessionKey == null || !userTable.containsKey(sessionKey)) {
                ctx.status(401).result("Invalid session key");
                return;
            }

            String userId = userTable.get(sessionKey);
            // Retrieve user information from the database or other source
            // For example:
            // User user = db.getUserById(userId);

            ctx.status(200).json(new StructuredResponse("ok", "User information", userId));
        });

        // Logout endpoint
        app.post("/logout", ctx -> {
            ctx.removeCookie("auth_token");
            ctx.removeCookie("session_key");
            ctx.redirect("/test");
        }); 
    }

    private static String uploadFileToDrive(Drive driveService, java.io.File file, String fileName, String contentType) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(fileName);

        FileContent mediaContent = new FileContent(contentType, file);
        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
        return uploadedFile.getId();
    }

    private static Drive getDriveService() {
        try {
            System.out.println("Current working directory: " + new java.io.File(".").getCanonicalPath());
            GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream("config/service-account-key.json"))
                    .createScoped(Collections.singleton(DriveScopes.DRIVE_FILE));

            return new Drive.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    credential)
                    .setApplicationName("The Buzz")
                    .build();
        } catch (Exception e) {
            // Log the error to the console
            System.err.println("Failed to create Drive service: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}