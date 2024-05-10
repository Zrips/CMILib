package net.Zrips.CMILib.Future;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CMIFutureBatcher {
    private Queue<CompletableFuture<?>> queue = new ConcurrentLinkedQueue<>();
    private boolean running = false;
    private int delayBetweenBatches = 0;

    public CompletableFuture<?> addBatch(CompletableFuture<?> future) {
        queue.add(future);
        processQueue();
        return future;
    }

    public void processQueue() {
        if (running && !queue.isEmpty())
            return;
        running = true;
        CompletableFuture.runAsync(() -> {
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
            running = false;
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
        return running;
    }
}
