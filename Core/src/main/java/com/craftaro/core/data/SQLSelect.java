package com.craftaro.core.data;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.SelectSelectStep;
import org.jooq.SelectWhereStep;
import org.jooq.impl.DSL;

public class SQLSelect extends SQLBase {

    private Select<?> currentStep;

    public SQLSelect(DSLContext ctx) {
        super(ctx);
        this.currentStep = ctx.select();
    }

    public static SQLSelect create(DSLContext ctx) {
        return new SQLSelect(ctx);
    }

    public SQLSelect select(String... fields) {
        if (fields.length > 0) {
            currentStep = ctx.select(DSL.field(fields[0]));
            for (int i = 1; i < fields.length; i++) {
                currentStep = ((SelectSelectStep<?>)currentStep).select(DSL.field(fields[i]));
            }
        } else {
            currentStep = ctx.select();
        }
        return this;
    }

    public SQLSelect from(String table) {
        currentStep = ((SelectSelectStep<?>)currentStep).from(DSL.table(table));
        return this;
    }

    public void from(String table, SQLResult.SQLResultI result) {
        Result<?> resultData = ((SelectSelectStep<?>)currentStep).from(DSL.table(table)).fetch();
        SQLResult rs = new SQLResult(resultData);
        for (SQLResult.StoredRecord record : rs.getResults()) {
            result.forEach(record);
        }
    }

    public SQLWhere where(String id, Object value) {
        currentStep = ((SelectWhereStep<?>)currentStep).where(DSL.field(id).eq(value));
        return new SQLWhere(ctx, currentStep);
    }

    public SQLWhere whereBetween(String id, Object value1, Object value2) {
        currentStep = ((SelectWhereStep<?>)currentStep).where(DSL.field(id).between(value1, value2));
        return new SQLWhere(ctx, currentStep);
    }
}
