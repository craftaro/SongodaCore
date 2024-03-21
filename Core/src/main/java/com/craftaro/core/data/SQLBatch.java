package com.craftaro.core.data;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SQLBatch implements SavesData {

    private final List<SavesData> batch = new LinkedList<>();

    public SQLBatch add(SavesData... data) {
        batch.addAll(Arrays.asList(data));
        return this;
    }

    public SQLBatch addAll(Collection<? extends SavesData> data) {
        batch.addAll(data);
        return this;
    }

    public List<SavesData> getBatch() {
        return batch;
    }

    @Override
    public void save(String... columns) {
        DatabaseManager.getInstance().getDatabaseConnector().connect(ctx -> {
            for (SavesData data : batch)
                data.saveImpl(ctx, columns);
        });
    }

    @Override
    public void saveImpl(DSLContext ctx, String... columns) {
        for (SavesData data : batch)
            data.saveImpl(ctx, columns);
    }

    @Override
    public void deleteImpl(DSLContext ctx) {
        for (SavesData data : batch)
            data.deleteImpl(ctx);
    }

}
