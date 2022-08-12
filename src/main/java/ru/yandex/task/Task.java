package ru.yandex.task;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String name;
    protected String descriptionTask;
    protected int id;
    protected TaskStatus statusTask;
    protected TasksType tasksType;
    protected LocalDateTime startDate;
    protected long durationInMinutes;

    public Task(String nameTask, String descriptionTask) {
        this.name = nameTask;
        this.descriptionTask = descriptionTask;
        this.tasksType = TasksType.TASK;
    }

    public Task() {
    }

    public Task(String name, String descriptionTask, LocalDateTime startDate, long durationInMinutes) {
        this.name = name;
        this.descriptionTask = descriptionTask;
        this.startDate = startDate;
        this.durationInMinutes = durationInMinutes;
        this.tasksType = TasksType.TASK;
    }

    public String getNameTask() {
        return name;
    }

    public void setNameTask(String nameTask) {
        this.name = nameTask;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }

    public void setDescriptionTask(String descriptionTask) {
        this.descriptionTask = descriptionTask;
    }

    public int getId() {
        return id;
    }

    public void setId(int identificationNumber) {
        this.id = identificationNumber;
    }

    public TaskStatus getStatusTask() {
        return statusTask;
    }

    public TasksType getTasksType() {
        return tasksType;
    }

    public void setStatusTask(TaskStatus statusTask) {
        this.statusTask = statusTask;
    }

    public void setTasksType(TasksType tasksType) {
        this.tasksType = tasksType;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return startDate == null ? null : startDate.plusMinutes(durationInMinutes);
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public long getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(long durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    @Override
    public String toString() {
        return "Task{" +
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
