package ru.yandex.managers;

import ru.yandex.exceptions.ManagerSaveException;
import ru.yandex.task.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter("src/main/resources/tasks.csv")) {
            fileWriter.write("id,type,name,status,description,start_date,duration,end_date,epic" + System.lineSeparator());
            for (Task task : getTasks().values()) {
                fileWriter.write(toString(task) + System.lineSeparator());
            }
            for (Epic epic : getEpics().values()) {
                fileWriter.write(toString(epic) + System.lineSeparator());

            }
            for (Epic epic : getEpics().values()) {
                for (Subtask subtask : epic.getSubtasks()) {
                    fileWriter.write(toString(subtask) + "," + epic.getId() + System.lineSeparator());
                }
            }
            fileWriter.write(System.lineSeparator());
            fileWriter.write(toStringHistory(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла.", e.getCause());
        }
    }

    public void loadFromFile(File file) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            skipFirstLine(fileReader);
            boolean isHistoryRead = false;
            while (fileReader.ready() && !isHistoryRead) {
                String lines = fileReader.readLine();
                if (!lines.isBlank()) {
                    if (fromStringTask(lines).getTasksType() == TasksType.TASK) {
                        getTasks().put(fromStringTask(lines).getId(), fromStringTask(lines));
                    } else if (fromStringTask(lines).getTasksType() == TasksType.EPIC) {
                        getEpics().put(fromStringTask(lines).getId(), (Epic) fromStringTask(lines));
                    } else if (fromStringTask(lines).getTasksType() == TasksType.SUBTASK) {
                        String[] values = lines.split(",");
                        Epic epic = getEpics().get(Integer.parseInt(values[8]));
                        epic.getSubtasks().add(fromStringTask(lines).getId(), (Subtask) fromStringTask(lines));
                    }
                } else {
                    String line = fileReader.readLine();
                    isHistoryRead = true;
                    for (Integer i : fromStringHistory(line)) {
                        if (getTasks().containsKey(i)) {
                            getTaskForId(i);
                        } else if (getEpics().containsKey(i)) {
                            getEpicForId(i);
                        } else {
                            for (Epic epic : getEpics().values()) {
                                if (epic.getSubtasks().contains(i)) {
                                    getSubtaskForId(i);
                                }
                            }
                        }
                    }
                    System.out.println(getHistoryManager());
                }
            }
            System.out.println(getTasks());
            System.out.println(getEpics());
            for (Epic epic : getEpics().values()) {
                System.out.println(epic.getSubtasks() + "," + epic.getId());
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка при чтении файла");
        }
    }

    private void skipFirstLine(BufferedReader reader) {
        for (int i = 0; i < 1; i++) {
            try {
                reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Task fromStringTask(String value) {
        String[] values = value.split(",");
        if (TasksType.valueOf(values[1]) == TasksType.TASK) {
            Task task = new Task(values[2], values[4]);
            task.setId(Integer.parseInt(values[0]));
            task.setTasksType(TasksType.valueOf(values[1]));
            task.setStatusTask(TaskStatus.valueOf(values[3]));
            String startDateString = values[5];
            LocalDateTime startDate = "null".equals(startDateString) ? null : LocalDateTime.parse(startDateString);
            task.setStartDate(startDate);
            task.setDurationInMinutes(Long.parseLong(values[6]));
            return task;
        } else if (TasksType.valueOf(values[1]) == TasksType.EPIC) {
            Epic epic = new Epic(values[2], values[4]);
            epic.setId(Integer.parseInt(values[0]));
            epic.setTasksType(TasksType.valueOf(values[1]));
            epic.setStatusTask(TaskStatus.valueOf(values[3]));
            String startDateString = values[5];
            LocalDateTime startDate = "null".equals(startDateString) ? null : LocalDateTime.parse(startDateString);
            epic.setStartDate(startDate);
            epic.setDurationInMinutes(Long.parseLong(values[6]));
            return epic;
        } else {
            Subtask subtask = new Subtask(values[2], values[4]);
            subtask.setId(Integer.parseInt(values[0]));
            subtask.setTasksType(TasksType.valueOf(values[1]));
            subtask.setStatusTask(TaskStatus.valueOf(values[3]));
            String startDateString = values[5];
            LocalDateTime startDate = "null".equals(startDateString) ? null : LocalDateTime.parse(startDateString);
            subtask.setStartDate(startDate);
            subtask.setDurationInMinutes(Long.parseLong(values[6]));
            return subtask;
        }
    }

    private String toString(Task task) {
        return task.getId() + "," + task.getTasksType() + "," + task.getNameTask() + "," + task.getStatusTask()
                + "," + task.getDescriptionTask() + "," + task.getStartDate() + "," + task.getDurationInMinutes()
                + "," + task.getEndDate();
    }

    private String toStringHistory(HistoryManager manager) {
        List<String> ids = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            int id = task.getId();
            ids.add(String.valueOf(id));
        }
        return String.join(",", ids);
    }

    private List<Integer> fromStringHistory(String value) {
        String[] values = value.split(",");
        List<Integer> taskList = new ArrayList<>();
        for (String num : values) {
            taskList.add(Integer.parseInt(num));
        }
        return taskList;
    }

    @Override
    public void deleteAll(){
        super.deleteAll();
        save();
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        save();
    }

    @Override
    public Task getTaskForId(int id) {
        Task task = super.getTaskForId(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicForId(int id) {
        Epic epic = super.getEpicForId(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskForId(int id) {
        Subtask subtask = super.getSubtaskForId(id);
        save();
        return subtask;
    }
}
