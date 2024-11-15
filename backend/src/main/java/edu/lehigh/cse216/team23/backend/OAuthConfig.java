package edu.lehigh.cse216.team23.backend;

/**
 * OAuthConfig is a class that contains the configuration for OAuth
 */
public class OAuthConfig {
    /**
     * Get the value of an environment variable
     * 
     * @param envVar The name of the environment variable
     * @return The value of the environment variable, or an empty string if not set
     */
    private static String getEnvVar(String envVar) {
        String value = System.getenv(envVar);
        if (value == null) {
            System.err.println("Environment variable " + envVar + " is not set.");
            return "";
        }
        return value;
    }

    /**
     * Get the client ID from environment variables
     * 
     * @return The client ID
     */
    public static String getClientId() {
        return getEnvVar("CLIENT_ID");
    }

    /**
     * Get the client secret from environment variables
     * 
     * @return The client secret
     */
    public static String getClientSecret() {
        return getEnvVar("CLIENT_SECRET");
    }

    /**
     * Get the redirect URI from environment variables
     * 
     * @return The redirect URI
     */
    public static String getRedirectUri() {
        return getEnvVar("REDIRECT_URI");
    }

    /**
     * Get the port
     * 
     * @return The port
     */
    public static String getPort() {
        return getEnvVar("URI_PORT");
    }
}