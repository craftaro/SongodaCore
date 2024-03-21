package com.craftaro.core.data.lazy;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class LazyList<T> {

    private final List<T> list;
    private List<Lazy<T>> lazyList;

    public LazyList(List list) {
        this.list = list == null ? new LinkedList<>() : list;
        this.lazyList = new LinkedList<>();
    }

    public List<T> getList() {
        loadList();
        return list;
    }

    public Set<T> getSet() {
        loadList();
        return new HashSet<>(list);
    }

    public void add(Supplier<T> supplier) {
        if (lazyList == null)
            list.add(supplier.get());
        else if (supplier != null)
            lazyList.add(new Lazy<T>().set(supplier));
    }

    public void add(T items) {
        list.add(items);
    }

    public void addAll(List<T> items) {
        list.addAll(items);
    }

    public void addAll(Supplier<T>... suppliers) {
        for (Supplier<T> supplier : suppliers)
            add(supplier);
    }

    private void loadList() {
        if (lazyList == null) return;
        for (Lazy<T> lazy : lazyList)
            list.add(lazy.get());
        lazyList = null;
    }

    public void clear() {
        list.clear();
        lazyList.clear();
    }

    public boolean isEmpty() {
        if (lazyList != null)
            return lazyList.isEmpty();
        return list.isEmpty();
    }
}
