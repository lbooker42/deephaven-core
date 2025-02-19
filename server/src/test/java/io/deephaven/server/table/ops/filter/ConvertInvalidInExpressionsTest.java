//
// Copyright (c) 2016-2025 Deephaven Data Labs and Patent Pending
//
package io.deephaven.server.table.ops.filter;

import io.deephaven.proto.backplane.grpc.CaseSensitivity;
import io.deephaven.proto.backplane.grpc.CompareCondition;
import io.deephaven.proto.backplane.grpc.Condition;
import org.junit.Test;

import static io.deephaven.server.table.ops.filter.FilterTestUtils.*;

public class ConvertInvalidInExpressionsTest extends AbstractNormalizingFilterTest {
    @Test
    public void testConvertInvalidInExpressions() {
        assertUnchanged("already correct",
                in("ColumnA", 1));
        assertUnchanged("already correct",
                in("ColumnA", 1, 2));

        assertFilterEquals("literals on both sides",
                in(literal(1), literal(2)),
                NormalizeFilterUtil.doComparison(CompareCondition.CompareOperation.EQUALS, CaseSensitivity.MATCH_CASE,
                        literal(1), literal(2)));
        assertFilterEquals("references on both sides",
                in(reference("ColumnA"), reference("ColumnB")),
                NormalizeFilterUtil.doComparison(CompareCondition.CompareOperation.EQUALS, CaseSensitivity.MATCH_CASE,
                        reference("ColumnA"), reference("ColumnB")));
    }

    @Override
    protected Condition execute(Condition f) {
        return ConvertInvalidInExpressions.exec(f);
    }
}
