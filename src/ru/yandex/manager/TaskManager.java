package ru.yandex.manager;

import ru.yandex.task.Epic;
import ru.yandex.task.Subtask;
import ru.yandex.task.Task;

import java.util.List;

public interface TaskManager {
    void addNewTask(Task task);

    void updateTask(Task task);

    List<Subtask> getEpicSubTask(Epic epic);

    void printAll();

    void printEpic(String nameEpic);

    void deleteAll();

    Task getTaskForId(int id);

    Epic getEpicForId(int id);

    Subtask getSubtaskForId(int id);

    void deleteTaskForId(int id);

    void deleteEpicForId(int id);

    void deleteSubTaskForId(int id);

    HistoryManager getHistoryManager();
}
