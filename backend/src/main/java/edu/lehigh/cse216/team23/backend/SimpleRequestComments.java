package edu.lehigh.cse216.team23.backend;

    /**
     * SimpleRequest provides a format for clients to present comment
     * strings to the server.
     * @param mComment The comment being provided by the client.
     */
    public record SimpleRequestComments( String mComment ) {}
