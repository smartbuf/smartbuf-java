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
public class ActiveHeap<T> {

    /**
     * final number
     */
    private int count;
    /**
     * unactive ContextName heap
     */
    private final ActiveRef[] heap;

    /**
     * Initialize NameHeap
     *
     * @param num final number
     */
    public ActiveHeap(int num) {
        this.heap = new ActiveRef[num];
    }

    /**
     * Filter the specified ContextName, decide whether to collect it or not.
     *
     * @param name ContextName
     */
    public void filter(ActiveRef<T> name) {
        if (count < heap.length) {
            this.heap[count++] = name;
            if (count == heap.length) {
                for (int i = heap.length / 2; i >= 0; i--) {
                    this.headAdjust(i, heap.length); // adjuest all
                }
            }
        } else if (culScore(this.heap[0]) > culScore(name)) {
            this.heap[0] = name;
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
            ActiveRef ref = heap[i];
            consumer.accept((T) ref.getData());
        }
    }

    // Adjust the specified heap from root
    private void headAdjust(int root, int len) {
        int parent = root;
        int child = 2 * parent + 1;
        for (; child < len; parent = child, child = 2 * parent + 1) {
            // select the bigger child
            if (child + 1 < len && culScore(heap[child]) < culScore(heap[child + 1])) {
                child = child + 1;
            }
            // break on stable heap
            if (culScore(heap[parent]) >= culScore(heap[child])) {
                break;
            }
            // exchange father and child
            ActiveRef tmp = heap[child];
            heap[child] = heap[parent];
            heap[parent] = tmp;
        }
    }

    // calculate ranking score of the specified ContextName
    private double culScore(ActiveRef name) {
        return name.getRcount() + name.getRtime() / 86400.0;
    }

}
