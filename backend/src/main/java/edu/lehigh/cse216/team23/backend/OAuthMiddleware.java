package edu.lehigh.cse216.team23.backend;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import io.javalin.http.Handler;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;

public class OAuthMiddleware implements Handler {
    @Override
    public void handle(Context ctx) throws Exception {
        String token = ctx.header("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new UnauthorizedResponse("Missing or invalid Authorization header");
        }

        token = token.substring(7); // Remove "Bearer " prefix

        try {
            OAuthConfig.verifyToken(token, new NetHttpTransport(), GsonFactory.getDefaultInstance(), "YOUR_CLIENT_ID");
        } catch (Exception e) {
            throw new UnauthorizedResponse("Invalid token");
        }
    }
}
