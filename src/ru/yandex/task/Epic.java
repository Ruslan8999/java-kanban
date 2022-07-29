package ru.yandex.task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks = new ArrayList<>();

    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask);
        this.tasksType = TasksType.EPIC;
    }

    public void setStatusTask(TaskStatus taskStatus) {
        this.statusTask = taskStatus;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public void addSubtask(Subtask subtask) {
        if (subtask != null) {
            subtasks.add(subtask);
        } else {
            System.out.println("Нельзя добавить пустую подзадачу");
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", id=" + id +
                ", statusTask=" + statusTask +
                ", tasksType=" + tasksType +
                '}';
    }
}
