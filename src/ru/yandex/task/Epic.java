package ru.yandex.task;

public class Epic extends Task {
    public Epic(String nameTask, String descriptionTask, int identificationNumber) {
        super(nameTask, descriptionTask, identificationNumber);
    }
    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask);
    }

    public void setStatusTask(TaskStatus taskStatus) {
        this.statusTask = taskStatus;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "nameEpic='" + name + '\'' +
                ", descriptionEpic='" + descriptionTask + '\'' +
                ", id=" + id +
                ", statusEpic=" + statusTask +
                '}';
    }
}
