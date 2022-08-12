package ru.yandex.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks = new ArrayList<>();
    protected LocalDateTime endDate;

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
            recalculateDates();
        } else {
            System.out.println("Нельзя добавить пустую подзадачу");
        }
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void recalculateDates() {
        if (subtasks.isEmpty()) {
            return;
        }
        long duration = subtasks.get(0).getDurationInMinutes();
        LocalDateTime startDateToUpdate = subtasks.get(0).getStartDate();
        LocalDateTime endDateToUpdate = subtasks.get(0).getEndDate();
        for (int i = 1; i < subtasks.size(); i++) {
            final Subtask subtask = subtasks.get(i);
            duration += subtask.getDurationInMinutes();
            final LocalDateTime subtaskStartDate = subtask.getStartDate();
            if (subtaskStartDate != null && subtaskStartDate.isBefore(startDateToUpdate)) {
                startDateToUpdate = subtaskStartDate;
            }
            final LocalDateTime subtaskEndDate = subtask.getEndDate();
            if (subtaskEndDate != null && subtaskEndDate.isAfter(endDateToUpdate)) {
                endDateToUpdate = subtaskEndDate;
            }
        }
        setDurationInMinutes(duration);
        setStartDate(startDateToUpdate);
        setEndDate(endDateToUpdate);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", id=" + id +
                ", statusTask=" + statusTask +
                ", tasksType=" + tasksType +
                ", startDate=" + startDate +
                ", durationInMinutes=" + durationInMinutes +
                ", endDate=" + endDate +
                '}';
    }
}
