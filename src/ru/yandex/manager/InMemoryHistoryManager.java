package ru.yandex.manager;

import ru.yandex.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> taskList;

    public InMemoryHistoryManager() {
        taskList = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        taskList.add(task);
        if (taskList.size() > 10) {
            taskList.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return taskList;
    }
}