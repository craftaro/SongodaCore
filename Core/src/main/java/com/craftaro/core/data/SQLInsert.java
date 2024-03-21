package com.craftaro.core.data;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.InsertOnDuplicateSetMoreStep;
import org.jooq.InsertOnDuplicateSetStep;
import org.jooq.InsertValuesStepN;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SQLInsert extends SQLBase {

    private String table;
    private final Map<String, Object> fields = new LinkedHashMap<>();
    private boolean voidedKey = false;

    private InsertValuesStepN currentStep;

    public SQLInsert(DSLContext ctx) {
        super(ctx);
    }

    public static SQLInsert create(DSLContext ctx) {
        return new SQLInsert(ctx);
    }

    public SQLInsert insertInto(String table) {
        this.table = table;
        return this;
    }

    public SQLInsert withField(String field, Object value, boolean voidKey) {
        if (voidKey)
            voidedKey = true;
        return voidKey ? this : withField(field, value);
    }

    public SQLInsert withField(String field, Object value) {
        if (field.equalsIgnoreCase("desc"))
            field = "`desc`";
        fields.put(field.toLowerCase(), value);
        return this;
    }

    // For some reason this must be used before we submit. So I'm going to apply the values here.
    public SQLExecutable onDuplicateKeyUpdate(String... columns) {
        currentStep = ctx.insertInto(DSL.table(table), fields.keySet().stream().map(DSL::field).toArray(Field[]::new));
        currentStep = currentStep.values(fields.values().stream().map(this::cleanValue).toArray());

        if (voidedKey)
            return new SQLExecutable(ctx, currentStep);

        SQLOnDupeUpdate sqlOnDupeUpdate = new SQLOnDupeUpdate(ctx, currentStep, columns);
        for (String column : (columns.length > 0 ? new HashSet<>(Arrays.asList(columns)) : fields.keySet()))
            sqlOnDupeUpdate.set(column.replace("`", ""), fields.get(column.toLowerCase()));
        return sqlOnDupeUpdate;
    }

    public static class SQLOnDupeUpdate extends SQLExecutable {

        private InsertOnDuplicateSetStep currentStep;
        private final List<String> columnsToUpdate = new ArrayList<>();


        public SQLOnDupeUpdate(DSLContext ctx, InsertValuesStepN currentStep, String... columns) {
            super(ctx, null);
            this.currentStep = currentStep.onDuplicateKeyUpdate();
            columnsToUpdate.addAll(Arrays.asList(columns));
        }

        public SQLOnDupeUpdate set(String field, Object value) {
            if (!columnsToUpdate.isEmpty() && !columnsToUpdate.contains(field))
                return this;

            value = cleanValue(value);
            Field fieldName = getField(field, value == null ? null : value.getClass());

            if (currentStep != null) {
                this.query = value == null ? this.currentStep.setNull(fieldName)
                        : this.currentStep.set(fieldName, value);
                currentStep = null;
            } else {
                this.query = value == null ? ((InsertOnDuplicateSetMoreStep) this.query).setNull(fieldName)
                        : ((InsertOnDuplicateSetMoreStep) this.query).set(fieldName, value);
            }

            return this;
        }
    }
}
