package com.songoda.core.http;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class HttpResponseImpl implements HttpResponse, AutoCloseable {
    protected final HttpURLConnection connection;

    protected byte[] body;

    HttpResponseImpl(HttpURLConnection connection) throws IOException {
        this.connection = connection;

        this.connection.connect();
    }

    public int getResponseCode() throws IOException {
        int statusCode = this.connection.getResponseCode();

        if (statusCode == -1) {
            throw new IOException("HTTP Status Code is -1");
        }

        return statusCode;
    }

    public byte[] getBody() throws IOException {
        if (this.body == null) {
            try (InputStream in = this.connection.getInputStream();
                 InputStream err = this.connection.getErrorStream()) {
                if (err != null) {
                    this.body = IOUtils.toByteArray(err);
                } else {
                    this.body = IOUtils.toByteArray(in);
                }
            }
        }

        return this.body;
    }

    public String getBodyAsString() throws IOException {
        return new String(getBody(), StandardCharsets.UTF_8);
    }

    @Override
    public void close() throws Exception {
        this.connection.disconnect();
    }
}
