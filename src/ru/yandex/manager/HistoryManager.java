package ru.yandex.manager;

import ru.yandex.task.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    List<Task> getHistory();
    void remove(int id);
}
