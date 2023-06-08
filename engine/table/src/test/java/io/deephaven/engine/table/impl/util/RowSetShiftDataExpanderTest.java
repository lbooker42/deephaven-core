/**
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
package io.deephaven.engine.table.impl.util;

import io.deephaven.engine.context.ExecutionContext;
import io.deephaven.engine.rowset.*;
import io.deephaven.engine.rowset.TrackingWritableRowSet;
import io.deephaven.engine.rowset.WritableRowSet;
import io.deephaven.engine.rowset.RowSetFactory;
import io.deephaven.engine.table.TableUpdate;
import io.deephaven.engine.table.impl.TableUpdateImpl;
import io.deephaven.engine.table.ModifiedColumnSet;
import io.deephaven.engine.testutil.junit4.EngineCleanup;
import io.deephaven.engine.updategraph.LogicalClockImpl;
import net.bytebuddy.build.Plugin;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RowSetShiftDataExpanderTest {

    @Rule
    final public EngineCleanup framework = new EngineCleanup();

    /**
     * These tests names have a few qualities worth defining: - Major Shift: shift with no overlap in keyspace before
     * and after (i.e. modified RowSet from shift alone is empty) - Minor Shift: shift with overlap in keyspace before
     * and after (i.e. modified RowSet from shift alone is non-empty) - Inner Shift: shift occurs with valid elements
     * below and above keyspace both before and after - NoAdd / NoRm: Other than the shift, either the added or removed
     * (respectively) indexes will be empty. - WithGaps: There will be a gap on the leading edges of the shift. (need to
     * carefully categorize items near gap) - Modified Inner/Outer: Rows inside/outside of a shift were modified. -
     * Removed Inner/Overlapping: Rows inside/overlapping a shift were removed.
     *
     * The test case ideas were generated by drawing rectangles with various adds/removes on each side of a pre-shift
     * and post-shift.
     */

    // To reduce per-test overhead, we'll let JUnit take care of our context helper.
    private Context c;

    @Before
    public void before() {
        c = new Context();
    }

    @After
    public void after() {
        c.validate();
    }

    @Test
    public void testMajorRInnerShift() {
        c.sourceRowSet.insertRange(1, 1000);
        c.added.insertRange(200, 400);
        c.removed.insertRange(401, 800); // not allowed to reorder -- so pretend things inbetween are gone
        c.shifted.shiftRange(200, 400, 400);
        c.expectModified.insertRange(200, 400);
        c.expectModified.insertRange(600, 800);
        c.expectRemoved.insertRange(401, 599);
    }

    @Test
    public void testMajorLInnerShift() {
        c.sourceRowSet.insertRange(1, 1000);
        c.added.insertRange(600, 800);
        c.removed.insertRange(200, 599); // not allowed to reorder -- so pretend things inbetween are gone
        c.shifted.shiftRange(600, 800, -400);
        c.expectModified.insertRange(200, 400);
        c.expectModified.insertRange(600, 800);
        c.expectRemoved.insertRange(401, 599);
    }

    @Test
    public void testMinorRInnerShift() {
        c.sourceRowSet.insertRange(1, 1000);
        c.added.insertRange(200, 299);
        c.removed.insertRange(401, 500);
        c.shifted.shiftRange(200, 400, 100);
        c.expectModified.insertRange(200, 500);
    }

    @Test
    public void testMinorLInnerShift() {
        c.sourceRowSet.insertRange(1, 1000);
        c.added.insertRange(401, 500);
        c.removed.insertRange(200, 299);
        c.shifted.shiftRange(300, 500, -100);
        c.expectModified.insertRange(200, 500);
    }

    @Test
    public void testMajorRShiftNoAdd() {
        c.sourceRowSet.insertRange(1, 1000);
        c.removed.insertRange(401, 800);
        c.shifted.shiftRange(200, 400, 400);
        c.expectRemoved.insertRange(200, 599);
        c.expectModified.insertRange(600, 800);
    }

    @Test
    public void testMajorLShiftNoAdd() {
        c.sourceRowSet.insertRange(1, 1000);
        c.removed.insertRange(200, 599);
        c.shifted.shiftRange(600, 800, -400);
        c.expectRemoved.insertRange(401, 800);
        c.expectModified.insertRange(200, 400);
    }

    @Test
    public void testMinorRShiftNoAdd() {
        c.sourceRowSet.insertRange(1, 1000);
        c.removed.insertRange(401, 500);
        c.shifted.shiftRange(200, 400, 100);
        c.expectRemoved.insertRange(200, 299);
        c.expectModified.insertRange(300, 500);
    }

    @Test
    public void testMinorLShiftNoAdd() {
        c.sourceRowSet.insertRange(1, 1000);
        c.removed.insertRange(200, 299);
        c.shifted.shiftRange(300, 500, -100);
        c.expectRemoved.insertRange(401, 500);
        c.expectModified.insertRange(200, 400);
    }

    @Test
    public void testMajorRShiftNoRm() {
        c.sourceRowSet.insertRange(1, 400);
        c.added.insertRange(200, 299);
        c.shifted.shiftRange(200, 400, 400);
        c.expectAdded.insertRange(600, 800);
        c.expectRemoved.insertRange(300, 400);
        c.expectModified.insertRange(200, 299);
    }

    @Test
    public void testMajorLShiftNoRm() {
        c.sourceRowSet.insertRange(600, 1000);
        c.added.insertRange(701, 800);
        c.shifted.shiftRange(600, 800, -400);
        c.expectAdded.insertRange(200, 400);
        c.expectRemoved.insertRange(600, 700);
        c.expectModified.insertRange(701, 800);
    }

    @Test
    public void testMinorRShiftNoRm() {
        c.sourceRowSet.insertRange(1, 1000);
        c.added.insertRange(200, 299);
        c.shifted.shiftRange(200, 1000, 100);
        c.expectAdded.insertRange(1001, 1100);
        c.expectModified.insertRange(200, 1000);
    }

    @Test
    public void testMinorLShiftNoRm() {
        c.sourceRowSet.insertRange(600, 1000);
        c.added.insertRange(701, 800);
        c.shifted.shiftRange(600, 800, -100);
        c.expectAdded.insertRange(500, 599);
        c.expectModified.insertRange(600, 800);
    }

    @Test
    public void testMajorRShiftNoAddNoRm() {
        c.sourceRowSet.insertRange(400, 600);
        c.shifted.shiftRange(500, 600, 400);
        c.expectAdded.insertRange(900, 1000);
        c.expectRemoved.insertRange(500, 600);
    }

    @Test
    public void testMajorLShiftNoAddNoRm() {
        c.sourceRowSet.insertRange(400, 600);
        c.shifted.shiftRange(400, 500, -400);
        c.expectAdded.insertRange(0, 100);
        c.expectRemoved.insertRange(400, 500);
    }

    @Test
    public void testMinorRShiftNoAddNoRm() {
        c.sourceRowSet.insertRange(400, 600);
        c.shifted.shiftRange(500, 600, 50);
        c.expectAdded.insertRange(601, 650);
        c.expectRemoved.insertRange(500, 549);
        c.expectModified.insertRange(550, 600);
    }

    @Test
    public void testMinorLShiftNoAddNoRm() {
        c.sourceRowSet.insertRange(400, 600);
        c.shifted.shiftRange(400, 500, -50);
        c.expectAdded.insertRange(350, 399);
        c.expectRemoved.insertRange(451, 500);
        c.expectModified.insertRange(400, 450);
    }

    @Test
    public void testMajorRShiftWithGaps() {
        c.sourceRowSet.insertRange(0, 100); // bookend
        c.sourceRowSet.insertRange(200, 300); // shifting range
        c.sourceRowSet.insertRange(675, 725); // left destination
        c.sourceRowSet.insertRange(775, 825); // right destination
        c.sourceRowSet.insertRange(900, 1000); // bookend
        c.added.insertRange(175, 225);
        c.added.insertRange(275, 325);
        c.removed.insertRange(675, 725);
        c.removed.insertRange(775, 825);
        c.shifted.shiftRange(200, 300, 500);
        c.expectAdded.insertRange(175, 199);
        c.expectAdded.insertRange(301, 325);
        c.expectAdded.insertRange(726, 774);
        c.expectRemoved.insertRange(226, 274);
        c.expectRemoved.insertRange(675, 699);
        c.expectRemoved.insertRange(801, 825);
        c.expectModified.insertRange(200, 225);
        c.expectModified.insertRange(275, 300);
        c.expectModified.insertRange(700, 725);
        c.expectModified.insertRange(775, 800);
    }

    @Test
    public void testMajorLShiftWithGaps() {
        c.sourceRowSet.insertRange(0, 100); // bookend
        c.sourceRowSet.insertRange(175, 225); // left destination
        c.sourceRowSet.insertRange(275, 325); // right destination
        c.sourceRowSet.insertRange(700, 800); // shifting range
        c.sourceRowSet.insertRange(900, 1000); // bookend
        c.added.insertRange(675, 725);
        c.added.insertRange(775, 825);
        c.removed.insertRange(175, 225);
        c.removed.insertRange(275, 325);
        c.shifted.shiftRange(700, 800, -500);
        c.expectAdded.insertRange(226, 274);
        c.expectAdded.insertRange(675, 699);
        c.expectAdded.insertRange(801, 825);
        c.expectRemoved.insertRange(175, 199);
        c.expectRemoved.insertRange(301, 325);
        c.expectRemoved.insertRange(726, 774);
        c.expectModified.insertRange(200, 225);
        c.expectModified.insertRange(275, 300);
        c.expectModified.insertRange(700, 725);
        c.expectModified.insertRange(775, 800);
    }

    @Test
    public void testMinorRShiftWithGaps() {
        c.sourceRowSet.insertRange(0, 100); // bookend
        c.sourceRowSet.insertRange(200, 400); // shifting range
        c.sourceRowSet.insertRange(405, 425); // right disappearing
        c.sourceRowSet.insertRange(475, 525); // right dest
        c.sourceRowSet.insertRange(900, 1000); // bookend
        c.added.insertRange(175, 225);
        c.added.insertRange(275, 299);
        c.removed.insertRange(405, 425);
        c.removed.insertRange(475, 525);
        c.shifted.shiftRange(200, 400, 100);
        c.expectAdded.insertRange(175, 199);
        c.expectAdded.insertRange(401, 404);
        c.expectAdded.insertRange(426, 474);
        c.expectRemoved.insertRange(226, 274);
        c.expectRemoved.insertRange(501, 525);
        c.expectModified.insertRange(200, 225);
        c.expectModified.insertRange(275, 400);
        c.expectModified.insertRange(405, 425);
        c.expectModified.insertRange(475, 500);
    }

    @Test
    public void testMinorLShiftWithGaps() {
        c.sourceRowSet.insertRange(0, 100); // bookend
        c.sourceRowSet.insertRange(600, 800); // shifting range
        c.sourceRowSet.insertRange(575, 595); // left disappearing
        c.sourceRowSet.insertRange(475, 525); // left dest
        c.sourceRowSet.insertRange(900, 1000); // bookend
        c.added.insertRange(775, 825);
        c.added.insertRange(701, 725);
        c.removed.insertRange(575, 595);
        c.removed.insertRange(475, 525);
        c.shifted.shiftRange(600, 800, -100);
        c.expectAdded.insertRange(801, 825);
        c.expectAdded.insertRange(596, 599);
        c.expectAdded.insertRange(526, 574);
        c.expectRemoved.insertRange(726, 774);
        c.expectRemoved.insertRange(475, 499);
        c.expectModified.insertRange(500, 525);
        c.expectModified.insertRange(575, 595);
        c.expectModified.insertRange(600, 725);
        c.expectModified.insertRange(775, 800);
    }

    @Test
    public void testGappedRShift() {
        // not all keys in shift range are real
        final long s = 25;
        final long e = 49;
        final long dt = 100;
        c.sourceRowSet.insertRange(s, e);
        c.shifted.shiftRange(0, 99, dt);
        c.expectAdded.insertRange(s + dt, e + dt);
        c.expectRemoved.insertRange(s, e);
    }

    @Test
    public void testGappedLShift() {
        // not all keys in shift range are real
        final long s = 125;
        final long e = 149;
        final long dt = -100;
        c.sourceRowSet.insertRange(s, e);
        c.shifted.shiftRange(100, 199, dt);
        c.expectAdded.insertRange(s + dt, e + dt);
        c.expectRemoved.insertRange(s, e);
    }

    @Test
    public void testMajorRShiftModifiedInner() {
        c.sourceRowSet.insertRange(400, 600);
        c.modified.insertRange(750, 850);
        c.shifted.shiftRange(400, 600, 300);
        c.expectAdded.insertRange(700, 900);
        c.expectRemoved.insertRange(400, 600);
    }

    @Test
    public void testMajorLShiftModifiedInner() {
        c.sourceRowSet.insertRange(400, 600);
        c.modified.insertRange(150, 250);
        c.shifted.shiftRange(400, 600, -300);
        c.expectAdded.insertRange(100, 300);
        c.expectRemoved.insertRange(400, 600);
    }

    @Test
    public void testMinorRShiftModifiedInner() {
        c.sourceRowSet.insertRange(400, 600);
        c.modified.insertRange(550, 650);
        c.shifted.shiftRange(400, 600, 100);
        c.expectAdded.insertRange(601, 700);
        c.expectRemoved.insertRange(400, 499);
        c.expectModified.insertRange(500, 600);
    }

    @Test
    public void testMinorLShiftModifiedInner() {
        c.sourceRowSet.insertRange(400, 600);
        c.modified.insertRange(350, 450);
        c.shifted.shiftRange(400, 600, -100);
        c.expectAdded.insertRange(300, 399);
        c.expectRemoved.insertRange(501, 600);
        c.expectModified.insertRange(400, 500);

    }

    @Test
    public void testMajorRShiftModifiedOuter() {
        c.sourceRowSet.insertRange(0, 75); // bookend
        c.sourceRowSet.insertRange(400, 600); // shifting
        c.sourceRowSet.insertRange(925, 1000); // bookend
        c.modified.insertRange(25, 75);
        c.modified.insertRange(925, 975);
        c.shifted.shiftRange(400, 600, 300);
        c.expectAdded.insertRange(700, 900);
        c.expectRemoved.insertRange(400, 600);
        c.expectModified.insert(c.modified);
    }

    @Test
    public void testMajorLShiftModifiedOuter() {
        c.sourceRowSet.insertRange(0, 75); // bookend
        c.sourceRowSet.insertRange(400, 600); // shifting
        c.sourceRowSet.insertRange(925, 1000); // bookend
        c.modified.insertRange(25, 75);
        c.modified.insertRange(925, 975);
        c.shifted.shiftRange(400, 600, -300);
        c.expectAdded.insertRange(100, 300);
        c.expectRemoved.insertRange(400, 600);
        c.expectModified.insert(c.modified);
    }

    @Test
    public void testMinorRShiftModifiedOuter() {
        c.sourceRowSet.insertRange(0, 75); // bookend
        c.sourceRowSet.insertRange(400, 600); // shifting
        c.sourceRowSet.insertRange(925, 1000); // bookend
        c.modified.insertRange(25, 75);
        c.modified.insertRange(925, 975);
        c.shifted.shiftRange(400, 600, 100);
        c.expectAdded.insertRange(601, 700);
        c.expectRemoved.insertRange(400, 499);
        c.expectModified.insertRange(500, 600);
        c.expectModified.insert(c.modified);
    }

    @Test
    public void testMinorLShiftModifiedOuter() {
        c.sourceRowSet.insertRange(0, 75); // bookend
        c.sourceRowSet.insertRange(400, 600); // shifting
        c.sourceRowSet.insertRange(925, 1000); // bookend
        c.modified.insertRange(25, 75);
        c.modified.insertRange(925, 975);
        c.shifted.shiftRange(400, 600, -100);
        c.expectAdded.insertRange(300, 399);
        c.expectRemoved.insertRange(501, 600);
        c.expectModified.insertRange(400, 500);
        c.expectModified.insert(c.modified);
    }


    @Test
    public void testMajorRShiftRemovedInner() {
        c.sourceRowSet.insertRange(400, 600);
        c.removed.insertRange(450, 550);
        c.shifted.shiftRange(400, 600, 300);
        c.expectAdded.insertRange(700, 749);
        c.expectAdded.insertRange(851, 900);
        c.expectRemoved.insertRange(400, 600);
    }

    @Test
    public void testMajorLShiftRemovedInner() {
        c.sourceRowSet.insertRange(400, 600);
        c.removed.insertRange(450, 550);
        c.shifted.shiftRange(400, 600, -300);
        c.expectAdded.insertRange(100, 149);
        c.expectAdded.insertRange(251, 300);
        c.expectRemoved.insertRange(400, 600);
    }

    @Test
    public void testMinorRShiftRemovedInner() {
        c.sourceRowSet.insertRange(400, 600);
        c.removed.insertRange(450, 550);
        c.shifted.shiftRange(400, 600, 100);
        c.expectAdded.insertRange(651, 700);
        c.expectRemoved.insertRange(400, 499);
        c.expectRemoved.insertRange(550, 600);
        c.expectModified.insertRange(500, 549);
    }

    @Test
    public void testMinorLShiftRemovedInner() {
        c.sourceRowSet.insertRange(400, 600);
        c.removed.insertRange(450, 550);
        c.shifted.shiftRange(400, 600, -100);
        c.expectAdded.insertRange(300, 349);
        c.expectRemoved.insertRange(400, 450);
        c.expectRemoved.insertRange(501, 600);
        c.expectModified.insertRange(451, 500);
    }

    @Test
    public void testMajorRShiftRemovedOverlap() {
        c.sourceRowSet.insertRange(0, 400);
        c.removed.insertRange(150, 249);
        c.removed.insertRange(301, 400);
        c.shifted.shiftRange(200, 350, 300); // only really moving 250-300
        c.expectAdded.insertRange(550, 600);
        c.expectRemoved.insertRange(150, 400);
    }

    @Test
    public void testMajorLShiftRemovedOverlap() {
        c.sourceRowSet.insertRange(400, 800);
        c.removed.insertRange(400, 499);
        c.removed.insertRange(551, 649);
        c.shifted.shiftRange(450, 600, -300); // only really moving 500-550
        c.expectAdded.insertRange(200, 250);
        c.expectRemoved.insertRange(400, 649);
    }

    @Test
    public void testMinorRShiftRemovedOverlap() {
        c.sourceRowSet.insertRange(0, 350);
        c.removed.insertRange(50, 149);
        c.removed.insertRange(251, 350);
        c.shifted.shiftRange(100, 400, 100); // only really moving 150-250
        c.expectRemoved.insertRange(50, 249);
        c.expectModified.insertRange(250, 350);
    }

    @Test
    public void testMinorLShiftRemovedOverlap() {
        c.sourceRowSet.insertRange(100, 450);
        c.removed.insertRange(100, 199);
        c.removed.insertRange(351, 400);
        c.shifted.shiftRange(100, 400, -100); // only really moving 200-350
        c.expectRemoved.insertRange(251, 400);
        c.expectModified.insertRange(100, 250);
    }

    @Test
    public void testInplaceAddRemove() {
        c.sourceRowSet.insertRange(0, 10);
        c.added.insertRange(0, 10);
        c.removed.insertRange(0, 10);
        c.expectModified.insertRange(0, 10);
    }

    /**
     * 1. Add initial RowSet state to {@code sourceRowSet}. 2. Setup added/removed/modified/shifted as inputs to
     * RowSetShiftDataExpander. 3. Modify expected output ranges to expectAdded / expectRemoved / expectModified. 4.
     * Profit by letting @Before / @After clear context and run validate automagically.
     */
    private static class Context {
        public final TrackingWritableRowSet sourceRowSet =
                RowSetFactory.empty().toTracking();
        public final WritableRowSet added = RowSetFactory.empty();
        public final WritableRowSet removed = RowSetFactory.empty();
        public final WritableRowSet modified = RowSetFactory.empty();
        public final RowSetShiftData.Builder shifted = new RowSetShiftData.Builder();

        public final WritableRowSet expectAdded = RowSetFactory.empty();
        public final WritableRowSet expectRemoved = RowSetFactory.empty();
        public final WritableRowSet expectModified = RowSetFactory.empty();

        private final LogicalClockImpl clock;

        public Context() {
            clock = (LogicalClockImpl) ExecutionContext.getContext().getUpdateGraph().clock();
            clock.resetForUnitTests();
        }

        public void validate() {
            clock.startUpdateCycle();
            sourceRowSet.update(expectAdded, expectRemoved);
            clock.completeUpdateCycle();

            final RowSetShiftData shiftData = shifted.build();
            shiftData.validate();
            final TableUpdate update =
                    new TableUpdateImpl(added, removed, modified, shiftData, ModifiedColumnSet.ALL);
            final RowSetShiftDataExpander expander = new RowSetShiftDataExpander(update, sourceRowSet);
            expander.validate(update, sourceRowSet);

            assertEquals(expectAdded, expander.getAdded());
            assertEquals(expectRemoved, expander.getRemoved());
            assertEquals(expectModified, expander.getModified());
        }
    }
}
