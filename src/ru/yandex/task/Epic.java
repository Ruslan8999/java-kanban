package ru.yandex.task;

import java.util.ArrayList;
import java.util.List;
public class Epic extends Task {

    private List<Subtask> subtasks= new ArrayList<>();
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
