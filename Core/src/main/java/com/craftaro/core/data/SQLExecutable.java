package com.craftaro.core.data;

import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.ResultQuery;
import org.jooq.SelectSelectStep;
import org.jooq.UpdateReturningStep;

import java.util.List;
import java.util.stream.Stream;

public class SQLExecutable extends SQLBase {

    protected Query query;

    public SQLExecutable(DSLContext ctx, Query query) {
        super(ctx);
        this.query = query;
    }

    public int execute() {
        return query.execute();
    }

    public Stream<SQLResult.StoredRecord> get() {
        if (query instanceof ResultQuery) {
            ResultQuery<?> resultQuery = (ResultQuery<?>) query;
            Result<?> result = resultQuery.getResult();
            return result.stream().map(SQLResult::new).map(SQLResult::getResults).flatMap(List::stream);
        } else {
            throw new IllegalStateException("Query is not an instance of ResultQuery");
        }
    }

    public interface SQLResultI {
        void forEach(SQLResult.StoredRecord result);
    }
}
