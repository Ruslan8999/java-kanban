package ru.yandex.task;

public class Task {

    protected String name;
    protected String descriptionTask;
    protected int id;
    protected TaskStatus statusTask;

    public Task(String nameTask, String descriptionTask) {
        this.name = nameTask;
        this.descriptionTask = descriptionTask;
    }

    public Task(String nameTask, String descriptionTask, int identificationNumber) {
        this.name = nameTask;
        this.descriptionTask = descriptionTask;
        this.id = identificationNumber;
        this.statusTask = TaskStatus.NEW;
    }

    public Task() {
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

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", id=" + id +
                ", statusTask=" + statusTask +
                '}';
    }
}
