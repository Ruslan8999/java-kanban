package ru.yandex.manager;

import ru.yandex.task.Epic;
import ru.yandex.task.Subtask;
import ru.yandex.task.Task;
import ru.yandex.task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int taskCounter;
    private int subTaskCounter;
    private int epicTaskCounter;
    private final Map<Integer, Task> taskHashMap;
    private final Map<Integer, Subtask> subtaskHashMap;
    private final Map<Integer, Epic> epicHashMap;
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        taskCounter = 0;
        subTaskCounter = 0;
        epicTaskCounter = 0;
        taskHashMap = new HashMap<>();
        subtaskHashMap = new HashMap<>();
        epicHashMap = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public void addNewTask(Task task) {
        if (task == null) {
            System.out.println("Error! task = null");
            return;
        }
        if (task instanceof Epic) {
            task.setId(epicTaskCounter++);
            epicHashMap.put(task.getId(), (Epic) task);
        } else if (task instanceof Subtask) {
            task.setId(subTaskCounter++);
            subtaskHashMap.put(task.getId(), (Subtask) task);
            ((Subtask) task).getParent().addSubtask((Subtask) task);
            updateEpicStatus(((Subtask) task).getParent());
        } else {
            task.setId(taskCounter++);
            taskHashMap.put(task.getId(), task);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task == null) {
            System.out.println("Список задач пуст");
            return;
        }
        if (task instanceof Epic) {
            epicHashMap.put(task.getId(), (Epic) task);
        } else if (task instanceof Subtask) {
            if (subtaskHashMap.containsKey(task.getId())) {
                TaskStatus oldTaskStatus = subtaskHashMap.get(task.getId()).getStatusTask();
                subtaskHashMap.put(task.getId(), (Subtask) task);
                if (!oldTaskStatus.equals(task.getStatusTask())) {
                    updateEpicStatus(((Subtask) task).getParent());
                }
            } else {
                System.out.println("Неккоректные данные");
            }
        } else {
            taskHashMap.put(task.getId(), task);
        }
    }

    private void updateEpicStatus(Epic epic) {
        if (epic == null) {
            System.out.println("Список задач пуст");
            return;
        }
        List<Subtask> subtasks = getEpicSubTask(epic);
        if (subtasks.isEmpty()) {
            epic.setStatusTask(TaskStatus.NEW);
            return;
        }
        int newStatusCounter = 0;
        int doneStatusCounter = 0;
        for (Subtask subtask : subtasks) {
            switch (subtask.getStatusTask()) {
                case NEW:
                    newStatusCounter++;
                    break;
                case IN_PROGRESS:
                    break;
                case DONE:
                    doneStatusCounter++;
                    break;
            }
            if (newStatusCounter == subtasks.size()) {
                epic.setStatusTask(TaskStatus.NEW);
            } else if (doneStatusCounter == subtasks.size()) {
                epic.setStatusTask(TaskStatus.DONE);
            } else {
                epic.setStatusTask(TaskStatus.IN_PROGRESS);
            }
        }
    }

    @Override
    public List<Subtask> getEpicSubTask(Epic epic) {
        List<Subtask> subtasks = new ArrayList<>();
        if (epic == null) {
            System.out.println("Список эпиков пуст");
            return subtasks;
        }
        for (Subtask subtask : subtaskHashMap.values()) {
            if (subtask.getParent().getId() == epic.getId()) {
                subtasks.add(subtask);
            }
        }
        return subtasks;
    }

    @Override
    public void printAll() {
        for (Task task : taskHashMap.values()) {
            System.out.println(task);
            for (Epic epic : epicHashMap.values()) {
                System.out.println(epic);
                for (Subtask subtask : getEpicSubTask(epic)) {
                    System.out.println(subtask);
                }
            }
        }
    }

    @Override
    public void printEpic(String nameEpic) {
        for (Epic epic : epicHashMap.values()) {
            if (nameEpic.equals(epic.getNameTask())) {
                for (Subtask subtask : getEpicSubTask(epic)) {
                    System.out.println(subtask);
                }
            } else {
                System.out.println("Такого эпика нет");
            }
        }
    }

    @Override
    public void deleteAll() {
        if (taskHashMap != null) {
            taskHashMap.clear();
        } else {
            System.out.println("Список задач пуст");
        }
        if (epicHashMap != null) {
            epicHashMap.clear();
        } else {
            System.out.println("Список эпиков пуст");
        }
        if (subtaskHashMap != null) {
            subtaskHashMap.clear();
        } else {
            System.out.println("Список подзадач пуст");
        }
    }

    @Override
    public Task getTaskForId(int id) {
        if (id < taskHashMap.size() && id >= 0) {
            Task task = taskHashMap.get(id);
            historyManager.add(task);
            return task;
        } else {
            System.out.println("Неверно введен номер задачи");
            return null;
        }
    }

    @Override
    public Epic getEpicForId(int id) {
        if (id < epicHashMap.size() && id >= 0) {
            Epic epic = epicHashMap.get(id);
            historyManager.add(epic);
            return epic;
        } else {
            System.out.println("Неверно введен номер эпика");
            return null;
        }
    }

    @Override
    public Subtask getSubtaskForId(int id) {
        if (id < subtaskHashMap.size() && id >= 0) {
            Subtask subtask = subtaskHashMap.get(id);
            historyManager.add(subtask);
            return subtask;
        } else {
            System.out.println("Неверно введен номер подзадачи");
            return null;
        }
    }

    @Override
    public void deleteTaskForId(int id) {
        if (taskHashMap.containsKey(id)) {
            taskHashMap.remove(id);
            System.out.println("Задача с номером id='" + id + '\'' + " удалена.");
        } else {
            System.out.println("Такого номера задачи нет");
        }
    }

    @Override
    public void deleteEpicForId(int id) {
        if (epicHashMap.containsKey(id)) {
            epicHashMap.remove(id);
            System.out.println("Эпик с номером id='" + id + '\'' + " удален.");
        } else {
            System.out.println("Такого номера эпика нет");
        }
    }

    @Override
    public void deleteSubTaskForId(int id) {
        if (subtaskHashMap.containsKey(id)) {
            subtaskHashMap.remove(id);
            System.out.println("Подзадача с номером id='" + id + '\'' + " удалена.");
        } else {
            System.out.println("Такого номера подзадачи нет");
        }
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
