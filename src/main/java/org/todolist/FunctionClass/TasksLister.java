package org.todolist.FunctionClass;

import org.todolist.FuncMenuMgr;
import org.todolist.TaskClass;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

import static org.todolist.UserMessages.NO_TASK_MSG;

public class TasksLister extends FuncMenuMgr {

    /**
     * As this pool will be heavily used,
     * it will remain active until the program terminates.
     */
    private static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool();
    private static final int BATCH_SIZE = 1000;

    public static void listTasks(Deque<TaskClass> tasks) {
        if (tasks.isEmpty()) {
            System.out.println(NO_TASK_MSG.getMessage());
            return;
        }

        Deque<CompletableFuture<Void>> futures = new ArrayDeque<>(tasks.size() / BATCH_SIZE + 1);  // 優化初始容量
        Iterator<TaskClass> iterator = tasks.iterator();

        while (iterator.hasNext()) {
            int remainingTasks = Math.min(BATCH_SIZE, tasks.size());
            Deque<TaskClass> batch = new ArrayDeque<>(remainingTasks);

            for (int i = 0; i < BATCH_SIZE && iterator.hasNext(); i++) {
                batch.add(iterator.next());
            }

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                StringBuilder batchOutput = new StringBuilder(BATCH_SIZE); // 預設容量估算
                ListTasksTask task = new ListTasksTask(batch, batchOutput);
                FORK_JOIN_POOL.invoke(task);
                System.out.print(batchOutput);
            }, FORK_JOIN_POOL);

            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        System.gc();
    }

    public static void shutdown() {
        FORK_JOIN_POOL.shutdown();
        try {
            if (!FORK_JOIN_POOL.awaitTermination(60, TimeUnit.SECONDS)) {
                FORK_JOIN_POOL.shutdownNow();
                if (!FORK_JOIN_POOL.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Thread pool did not terminate.");
                }
            }
        } catch (InterruptedException e) {
            FORK_JOIN_POOL.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static class ListTasksTask extends RecursiveTask<Void> {
        private final Deque<TaskClass> list;
        private final StringBuilder batchOutput;

        ListTasksTask(Deque<TaskClass> list, StringBuilder batchOutput) {
            this.list = list;
            this.batchOutput = batchOutput;
        }

        @Override
        protected Void compute() {
            generateOutput(batchOutput);
            return null;
        }

        private void generateOutput(StringBuilder batchOutput) {
            for (TaskClass task : list) {
                batchOutput.append("----------------------------------------------------------------------------------------------------\n")
                        .append(String.format("Due Date: %-30s Task ID: %-32d Rank: %-10s\n\n",
                                task.getTimeScore() == 0 ? "No due date" : task.getDueDate(),
                                task.getTaskId(),
                                task.getPendingRank()))
                        .append(String.format("Content: %-50s\n\n", task.getContent()))
                        .append(String.format("Status: %-30s By %-35s ( %-15s )\n",
                                task.checkTaskCompleteStatus() ? "Completed" : "Pending",
                                task.getAuthor(),
                                task.getCreatedDate()))
                        .append("----------------------------------------------------------------------------------------------------\n");
            }
        }
    }
}
