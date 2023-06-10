package com.craftaro.core.http;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface HttpClient {
    @NotNull HttpResponse get(String url) throws IOException;
}
