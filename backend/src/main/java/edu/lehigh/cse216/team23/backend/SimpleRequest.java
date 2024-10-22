package edu.lehigh.cse216.team23.backend;

/**
 * SimpleRequest provides a format for clients to present title and message 
 * strings to the server.
 * @param mLikes The likes being provided by the client.
 * @param mMessage The message being provided by the client.
 */
public record SimpleRequest( int mLikes, String mMessage ) {}