package edu.lehigh.cse216.team23.backend;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.util.Collections;

public class CookieAuthMiddleware implements Handler {
    private static final String CLIENT_ID = "YOUR_CLIENT_ID";

    @Override
    public void handle(Context ctx) throws Exception {
        String token = ctx.cookie("auth_token");
        if (token == null) {
            throw new UnauthorizedResponse("Missing or invalid auth token");
        }

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken = verifier.verify(token);
        if (idToken == null) {
            throw new UnauthorizedResponse("Invalid token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String userId = payload.getSubject();  // User's unique ID

        // Store user information in the context for use in the endpoint
        ctx.attribute("userId", userId);
    }
}
