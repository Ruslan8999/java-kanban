package ru.yandex.manager;

import ru.yandex.task.Epic;
import ru.yandex.task.Subtask;
import ru.yandex.task.Task;

import java.util.List;

public interface TaskManager {
    public void addNewTask(Task task);

    public void updateTask(Task task);

    public void updateEpicStatus(Epic epic);

    public List<Subtask> getEpicSubTask(Epic epic);

    public void printAll();

    public void printEpic(String nameEpic);
    public void deleteAll();

    public Task getTaskForId(int id);

    public Epic getEpicForId(int id);

    public Subtask getSubtaskForId(int id);

    public void deleteTaskForId(int id);

    public void deleteEpicForId(int id);

    public void deleteSubTaskForId(int id);

}
