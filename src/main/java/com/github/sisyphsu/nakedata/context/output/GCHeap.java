package com.github.sisyphsu.nakedata.context.output;

import lombok.Getter;

import java.util.function.Consumer;

/**
 * Use max heap to collect unactived name.
 *
 * @author sulin
 * @since 2019-04-29 20:22:38
 */
@Getter
public class GCHeap<T extends GCHeap.Score> {

    /**
     * final number
     */
    private int count;
    /**
     * unactive ContextName heap
     */
    private final Score[] heap;

    /**
     * Initialize NameHeap
     *
     * @param num final number
     */
    public GCHeap(int num) {
        this.heap = new Score[num];
    }

    /**
     * Filter the specified ContextName, decide whether to collect it or not.
     *
     * @param t ContextName
     */
    public void filter(T t) {
        if (count < heap.length) {
            this.heap[count++] = t;
            if (count == heap.length) {
                for (int i = heap.length / 2; i >= 0; i--) {
                    this.headAdjust(i, heap.length); // adjuest all
                }
            }
        } else if (this.heap[0].getScore() > t.getScore()) {
            this.heap[0] = t;
            this.headAdjust(0, heap.length); // adjust once
        }
    }

    /**
     * 循环迭代
     *
     * @param consumer 循环函数
     */
    @SuppressWarnings("unchecked")
    public void forEach(Consumer<T> consumer) {
        for (int i = 0; i < count; i++) {
            Score item = heap[i];
            consumer.accept((T) item);
        }
    }

    // Adjust the specified heap from root
    private void headAdjust(int root, int len) {
        int parent = root;
        int child = 2 * parent + 1;
        for (; child < len; parent = child, child = 2 * parent + 1) {
            // select the bigger child
            if (child + 1 < len && heap[child].getScore() < heap[child + 1].getScore()) {
                child = child + 1;
            }
            // break on stable heap
            if (heap[parent].getScore() >= heap[child].getScore()) {
                break;
            }
            // exchange father and child
            Score tmp = heap[child];
            heap[child] = heap[parent];
            heap[parent] = tmp;
        }
    }

    public interface Score {

        int getCount();

        int getTime();

        default double getScore() {
            return this.getCount() + this.getTime() / 86400.0;
        }

    }

}
