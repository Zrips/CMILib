package net.Zrips.CMILib.Future;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class CMIFutureBatcher {
    private Queue<CompletableFuture<?>> queue = new ConcurrentLinkedQueue<>();
    private final AtomicBoolean running = new AtomicBoolean(false);
    private int delayBetweenBatches = 0;

    public CMIFutureBatcher() {

    }

    public CMIFutureBatcher(int delayBetweenBatches) {
        this.delayBetweenBatches = delayBetweenBatches;
    }

    public CompletableFuture<?> addBatch(CompletableFuture<?> future) {
        queue.add(future);
        processQueue();
        return future;
    }

    public void processQueue() {
        if (!running.compareAndSet(false, true))
            return;
        CompletableFuture.runAsync(() -> {
            while (true) {
                while (!queue.isEmpty()) {
                    CompletableFuture<?> future = queue.poll();

                    if (future == null)
                        break;

                    if (delayBetweenBatches > 0)
                        try {
                            Thread.sleep(delayBetweenBatches);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    future.complete(null);
                }
                running.set(false);
                if (!queue.isEmpty() && running.compareAndSet(false, true))
                    continue;
                break;
            }
        });
    }

    public int getDelayBetweenBatches() {
        return delayBetweenBatches;
    }

    public void setDelayBetweenBatches(int delayBetweenBatches) {
        this.delayBetweenBatches = delayBetweenBatches;
    }

    public int getQueueSize() {
        return queue.size();
    }

    public boolean isRunning() {
        return running.get();
    }
}
