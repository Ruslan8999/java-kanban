package ru.yandex.task;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private Epic parent;

    public Subtask(String nameTask, String descriptionTask, Epic parent, TaskStatus taskStatus) {
        if (parent != null) {
            this.name = nameTask;
            this.descriptionTask = descriptionTask;
            this.statusTask = taskStatus;
            this.parent = parent;
        } else {
            System.out.println("parent = null");
        }
    }

    public Subtask(String nameTask, String descriptionTask, Epic parent) {
        if (parent != null) {
            this.name = nameTask;
            this.descriptionTask = descriptionTask;
            this.statusTask = TaskStatus.NEW;
            this.parent = parent;
            this.tasksType = TasksType.SUBTASK;
        } else {
            System.out.println("parent = null");
        }
    }

    public Subtask(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask);
    }

    public Subtask(String name, String descriptionTask, LocalDateTime startDate, long durationInMinutes, Epic parent) {
        super(name, descriptionTask, startDate, durationInMinutes);
        this.parent = parent;
        this.statusTask = TaskStatus.NEW;
        this.tasksType = TasksType.SUBTASK;
    }

    public Epic getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", id=" + id +
                ", statusTask=" + statusTask +
                ", tasksType=" + tasksType +
                ", startDate=" + startDate +
                ", durationInMinutes=" + durationInMinutes +
                '}';
    }
}