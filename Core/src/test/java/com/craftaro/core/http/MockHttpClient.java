package com.craftaro.core.http;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class MockHttpClient implements HttpClient {
    public HttpResponse returnValue;
    public List<String> callsOnGet = new LinkedList<>();

    public MockHttpClient(HttpResponse returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public @NotNull HttpResponse get(String url) {
        this.callsOnGet.add(url);

        return this.returnValue;
    }
}
