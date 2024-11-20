package edu.lehigh.cse216.team23.backend;

    /**
     * SimpleRequestUser provides a format for clients to present user
    * strings to the server.
    * @param mUserId The user id being provided by the client.
    * @param mEmail The email being provided by the client.
    * @param mName The name being provided by the client.
    */
    public record SimpleRequestUser( String mUserId, String mEmail, String mName ) {
    }
