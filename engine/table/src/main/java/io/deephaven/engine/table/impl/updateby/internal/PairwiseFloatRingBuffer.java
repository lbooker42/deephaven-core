/**
 * Copyright (c) 2016-2022 Deephaven Data Labs and Patent Pending
 */
package io.deephaven.engine.table.impl.updateby.internal;

import io.deephaven.chunk.WritableFloatChunk;
import io.deephaven.chunk.attributes.Values;
import io.deephaven.util.SafeCloseable;

import java.util.BitSet;
import java.util.NoSuchElementException;

/***
 * Store this data in the form of a binary tree where the latter half of the chunk is treated as a ring buffer and pairwise results of the `FloatFunction` are stored in the parent nodes.  We do lazy evaluation by maintaining a 'dirty' `BitSet` and compute the ultimate pairwise result only when requested by `evaluate()'
 *
 * To keep the parent-node finding math easy and consistent between the ring buffer and the computation tree, the binary tree is shifted by one index so the root (and final result of computation) ends up in index 1 (instead of 0 which is un-used)
 */

public class PairwiseFloatRingBuffer implements SafeCloseable {
    // use a sized float chunk for underlying storage
    private WritableFloatChunk<Values> storageChunk;
    private final BitSet dirtyBits;
    private final FloatFunction pairwiseFunction;
    private final float emptyVal;

    // this measures internal storage capacity (chunk is twice this size)
    private int capacity;
    private int chunkSize;

    private int head;
    private int tail;

    @FunctionalInterface
    public interface FloatFunction {
        /**
         * Applies this function to the given arguments.
         *
         * @param a the first function argument
         * @param b the second function argument
         * @return the function result
         */
        float apply(float a, float b);
    }

    /**
     * Create a ring buffer for Float values that will perform pairwise evaluation of the internal data values using an efficient binary-tree implementation to compute only changed values.  The buffer will grow exponentially as items are pushed into it but will not shrink as values are removed
     *
     * @param initialSize the minimum size for the structure to hold
     * @param emptyVal an innocuous value that will not affect the user-provided function results. for example, 0.0f for performing addition/subtraction, 1.0f for performing multiplication/division
     * @param pairwiseFunction the user provided function for evaluation, takes two float parameters and returns a float. This function will be applied repeatedly to pairs of data values until the final result is available
     */
    public PairwiseFloatRingBuffer(int initialSize, float emptyVal, FloatFunction pairwiseFunction) {
        // increase to next power of two
        this.capacity = Integer.highestOneBit(initialSize) * 2;
        this.chunkSize = capacity * 2;
        this.storageChunk = WritableFloatChunk.makeWritableChunk(chunkSize);
        this.dirtyBits = new BitSet(chunkSize);
        this.pairwiseFunction = pairwiseFunction;
        this.emptyVal = emptyVal;

        this.storageChunk.fillWithValue(0, chunkSize, emptyVal);
        this.head = this.tail = this.capacity;
    }

    public float evaluate() {
        // work through all the dirty bits from high to low until none remain
        int bit = chunkSize;
        while (!dirtyBits.isEmpty()) {
            int nextSetBit = dirtyBits.previousSetBit(bit);
            final int left = nextSetBit & 0xFFFFFFFE; // clear the final bit to force evenness
            final int right = left + 1;
            // this isn't the typical parent = (n-1)/2 because the tree is right-shifted by one
            final int parent = left / 2;

            // load the data values
            final float leftVal = storageChunk.get(left);
            final float rightVal = storageChunk.get(right);
            final float parentVal = storageChunk.get(parent);

            dirtyBits.clear(left, right + 1); // clear() excludes `toIndex` so add one to clear `right` as well

            final float computeVal = pairwiseFunction.apply(leftVal, rightVal);
            if (parentVal != computeVal) {
                storageChunk.set(parent, computeVal);
                // mark the parent dirty (if not the last)
                if (parent > 1) {
                    dirtyBits.set(parent);
                }
            } else {
                final int x = 5;
            }
            bit = left;
        }
        // final value is in index 1
        return storageChunk.get(1);
    }

    private void grow() {
        int oldCapacity = capacity;
        int oldChunkSize = chunkSize;

        // double the current capacity
        capacity *= 2;
        chunkSize = capacity * 2;

        // transfer to the new chunk
        WritableFloatChunk<Values> oldChunk = storageChunk;
        storageChunk = WritableFloatChunk.makeWritableChunk(chunkSize);

        // fill the pairwise tree (0 to capacity) with empty value
        storageChunk.fillWithValue(0, capacity, emptyVal);

        // move the data to the new chunk, note that we store the ring data in the second half of the array
        if (tail > head) {
            storageChunk.copyFromTypedChunk(oldChunk, head, capacity, tail - head);
            tail = capacity + tail - head;
        } else {
            storageChunk.copyFromTypedChunk(oldChunk, head, capacity, oldChunkSize - head);
            storageChunk.copyFromTypedChunk(oldChunk, oldCapacity, oldChunkSize - head + capacity, tail - oldCapacity);
            tail = capacity + oldCapacity - 1;
        }
        // fill the unused storage with the empty value
        storageChunk.fillWithValue(tail, chunkSize - tail, emptyVal);

        // free the old data chunk
        oldChunk.close();
        head = capacity;

        // TODO: investigate moving precomputed results also.  Since we are re-ordering the data values, would be
        // tricky to maintain order but a recursive function could probably do it efficiently.  For now, make life easy
        // by setting all input dirty so the tree is recomputed on next `evaluate()`
        dirtyBits.clear();
        dirtyBits.set(head, tail, true);
    }

    public void push(float val) {
        if (isFull()) {
            grow();
        }
        // add the new data
        storageChunk.set(tail, val);
        dirtyBits.set(tail);
        // move the tail
        tail = ((tail + 1) % capacity) + capacity;
    }

    public void pushEmptyValue() {
        push(emptyVal);
    }

    public float pop() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        float val = storageChunk.get(head);
        storageChunk.set(head, emptyVal);
        dirtyBits.set(head);
        // move the head
        head = ((head + 1) % capacity) + capacity;
        return val;
    }

    public boolean isFull() {
        return ((tail + 1) % capacity) + capacity == head;
    }

    public int size() {
        return tail >= head
                ? (tail - head) :
                (tail + (capacity - head));
    }

    public boolean isEmpty() {
        return tail == head;
    }

    public float peek(float onEmpty) {
        if (isEmpty()) {
            return onEmpty;
        }
        return storageChunk.get(head);
    }

    public float poll(float onEmpty) {
        if (isEmpty()) {
            return onEmpty;
        }
        float e = storageChunk.get(head);
        head = (head + 1) % capacity + capacity;
        return e;
    }

    public float front() {
        return front(0);
    }

    public float front(int offset) {
        if (offset < 0 || offset >= size()) {
            throw new NoSuchElementException();
        }
        return storageChunk.get((head + offset) % capacity + capacity);
    }

    public float back() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return tail == capacity ? storageChunk.get(chunkSize - 1) : storageChunk.get(tail - 1);
    }

    public float peekBack(float onEmpty) {
        if (isEmpty()) {
            return onEmpty;
        }
        return tail == capacity ? storageChunk.get(chunkSize - 1) : storageChunk.get(tail - 1);
    }

    public float element() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return storageChunk.get(head);
    }

    public int capacity() {
        return capacity - 1;
    }

    public int remaining() {
        return capacity() - size();
    }

    @Override
    public void close() {
        try (final WritableFloatChunk<Values> ignoredChunk = storageChunk) {
            // close the closable items
        }
    }
}
