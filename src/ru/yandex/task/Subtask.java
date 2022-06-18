package ru.yandex.task;

public class Subtask extends Task {

    private Epic parent;

    public Subtask(String nameTask, String descriptionTask, int identificationNumber, Epic parent) {
        if (parent != null) {
            this.name = nameTask;
            this.descriptionTask = descriptionTask;
            this.id = identificationNumber;
            this.statusTask = TaskStatus.NEW;
            this.parent = parent;
        } else {
            System.out.println("parent = null");
        }
    }

    public Subtask(String nameTask, String descriptionTask, int identificationNumber, Epic parent, TaskStatus taskStatus) {
        if (parent != null) {
            this.name = nameTask;
            this.descriptionTask = descriptionTask;
            this.id = identificationNumber;
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
        } else {
            System.out.println("parent = null");
        }
    }

    public Epic getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "nameSubtask='" + name + '\'' +
                ", descriptionSubtask='" + descriptionTask + '\'' +
                ", id=" + id +
                ", statusSubtask=" + statusTask +
                '}';
    }
}
