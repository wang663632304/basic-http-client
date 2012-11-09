
package com.turbomanage.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;

/**
 * Interface that defines the request lifecycle used by {@link AbstractHttpClient}.
 * RequestHandler is composed of many sub-interfaces so that each handler can 
 * be set independently if needed. You can provide your own implementation by providing
 * it in the client constructor.
 * 
 * See {@link BasicRequestHandler} for a simple implementation.
 * 
 * @author David M. Chandler
 */
public interface RequestHandler {
    
    public static final String UTF8 = "UTF-8";

    /**
     * Opens an HTTP connection.
     * 
     * @param url Absolute URL
     * @return an open {@link HttpURLConnection}
     * @throws IOException
     */
    HttpURLConnection openConnection(String url) throws IOException;

    /**
     * Prepares a previously opened connection. It is called before
     * writing to the OutputStream, so it's possible to set or
     * modify connection properties. This is where to set the request method,
     * content type, timeouts, etc.
     * 
     * @param urlConnection An open connection
     * @param httpMethod The request method
     * @param contentType MIME type
     * @throws IOException
     */
    void prepareConnection(HttpURLConnection urlConnection, HttpMethod httpMethod,
            String contentType) throws IOException;

    /**
     * Writes to an open, prepared connection. This method is only called when
     * {@link HttpURLConnection#getDoOutput()} is true and there is non-null content. 
     * 
     * @param out An open {@link OutputStream}
     * @param content Data to send with the request
     * @throws IOException
     */
    void writeStream(OutputStream out, byte[] content) throws IOException;

    /**
     * Reads from the InputStream.
     * 
     * @param in An open {@link InputStream}
     * @return Object contained in the response
     * @throws IOException
     */
    byte[] readStream(InputStream in) throws IOException;

    /**
     * Invoked for any exceptions. Of particular interest are
     * {@link ConnectException}
     * {@link SocketTimeoutException}
     * 
     * @param e The exception that was thrown
     * @return true if recoverable. Async clients may use this to try again
     */
    boolean onError(HttpRequestException e);

    void checkReturnStatus(int status)throws HttpStatusException;
}
