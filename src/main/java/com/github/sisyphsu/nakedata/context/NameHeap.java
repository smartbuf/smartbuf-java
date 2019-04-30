package com.github.sisyphsu.nakedata.context;

/**
 * Use max heap to collect unactived name.
 *
 * @author sulin
 * @since 2019-04-29 20:22:38
 */
public class NameHeap {

    /**
     * final number
     */
    private int count;
    /**
     * unactive ContextName heap
     */
    private ContextName[] heap;

    /**
     * Initialize NameHeap
     *
     * @param num final number
     */
    public NameHeap(int num) {
        this.heap = new ContextName[num];
    }

    /**
     * Filter the specified ContextName, decide whether to collect it or not.
     *
     * @param name ContextName
     */
    public void filter(ContextName name) {
        if (count < heap.length) {
            this.heap[count++] = name;
            if (count == heap.length) {
                for (int i = heap.length / 2; i >= 0; i--) {
                    this.headAdjust(i, heap.length); // adjuest all
                }
            }
        } else if (this.heap[0].getScore() > name.getScore()) {
            this.heap[0] = name;
            this.headAdjust(0, heap.length); // adjust once
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
            ContextName tmp = heap[child];
            heap[child] = heap[parent];
            heap[parent] = tmp;
        }
    }

}
