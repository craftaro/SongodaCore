package com.craftaro.core.data.lazy;

import java.util.function.Supplier;

public class Lazy<T> {

    private Supplier<T> supplier = null;
    private T value = null;

    public synchronized T get() {
        if (value == null && supplier != null) {
            value = supplier.get();
            supplier = null;
        }
        return value;
    }

    public synchronized T getOrDefault(T def) {
        T value = get();
        return value == null ? def : value;
    }

    public synchronized Lazy<T> reset() {
        value = null;
        return this;
    }

    public synchronized Lazy<T> set(T value) {
        this.value = value;
        return this;
    }

    public Lazy<T> set(Supplier<T> supplier) {
        this.supplier = supplier;
        return this;
    }

    public synchronized boolean isLoaded() {
        return supplier != null;
    }

    @Override
    public String toString() {
        return get().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == get()) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof Lazy) {
            Lazy<?> other = (Lazy<?>) obj;
            return get().equals(other.get());
        }
        return get().equals(obj);
    }

    @Override
    public int hashCode() {
        return get().hashCode();
    }
}
