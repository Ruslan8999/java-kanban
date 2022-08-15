package ru.yandex.managers;

import ru.yandex.task.Epic;
import ru.yandex.task.Subtask;
import ru.yandex.task.Task;
import ru.yandex.task.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int taskCounter;
    private int subTaskCounter;
    private int epicTaskCounter;
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Subtask> subtasks;
    private final Map<Integer, Epic> epics;
    private final HistoryManager historyManager;
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartDate, Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId));

    public InMemoryTaskManager() {
        taskCounter = 0;
        subTaskCounter = 0;
        epicTaskCounter = 0;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public void addNewTask(Task task) {
        if (task == null) {
            System.out.println("Error! task = null");
            return;
        }
        if (checkCrossingTime(task) || prioritizedTasks.isEmpty()) {
            if (task instanceof Epic) {
                task.setId(epicTaskCounter++);
                epics.put(task.getId(), (Epic) task);
                task.setStatusTask(TaskStatus.NEW);
                prioritizedTasks.add(task);
            } else if (task instanceof Subtask) {
                task.setId(subTaskCounter++);
                subtasks.put(task.getId(), (Subtask) task);
                ((Subtask) task).getParent().addSubtask((Subtask) task);
                updateEpicStatus(((Subtask) task).getParent());
                prioritizedTasks.add(task);
            } else {
                task.setId(taskCounter++);
                tasks.put(task.getId(), task);
                task.setStatusTask(TaskStatus.NEW);
                prioritizedTasks.add(task);
            }
        } else {
            System.out.println("Error! Пересечение задачи " + task.getNameTask() + ". Задача не была добавлена");
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task == null) {
            System.out.println("Список задач пуст");
            return;
        }
        if (checkCrossingTime(task) || prioritizedTasks.isEmpty()) {
            if (task instanceof Epic) {
                epics.put(task.getId(), (Epic) task);
                prioritizedTasks.add(task);
            } else if (task instanceof Subtask) {
                if (subtasks.containsKey(task.getId())) {
                    TaskStatus oldTaskStatus = subtasks.get(task.getId()).getStatusTask();
                    subtasks.put(task.getId(), (Subtask) task);
                    if (!oldTaskStatus.equals(task.getStatusTask())) {
                        updateEpicStatus(((Subtask) task).getParent());
                        prioritizedTasks.add(task);
                    }
                } else {
                    System.out.println("Некорректные данные");
                }
            } else {
                tasks.put(task.getId(), task);
                prioritizedTasks.add(task);
            }
        } else {
            System.out.println("Error! Пересечение задачи " + task.getNameTask() + ". Задача не была добавлена");
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

    boolean checkCrossingTime(Task task) {
        boolean isNotCrossingTime = false;
        if (task.getStartDate() == null) {
            isNotCrossingTime = true;
        } else {
            for (Task element : prioritizedTasks) {
                if (element.getStartDate() == null) {
                    continue;
                }
                if (element.getId() == (task.getId())) {
                    isNotCrossingTime = true;
                    continue;
                }
                if (task.getStartDate().equals(element.getStartDate())) {
                    isNotCrossingTime = false;
                    break;
                }
                if (!task.getStartDate().isBefore(element.getEndDate()) || !task.getEndDate().isAfter(element.getStartDate())) {
                    isNotCrossingTime = true;
                } else {
                    isNotCrossingTime = false;
                    break;
                }
            }
        }
        return isNotCrossingTime;
    }

    @Override
    public List<Subtask> getEpicSubTask(Epic epic) {
        List<Subtask> subtasks = new ArrayList<>();
        if (epic == null) {
            System.out.println("Список эпиков пуст");
            return subtasks;
        }
        for (Subtask subtask : this.subtasks.values()) {
            if (subtask.getParent().getId() == epic.getId()) {
                subtasks.add(subtask);
            }
        }
        return subtasks;
    }

    @Override
    public void printAll() {
        for (Task task : tasks.values()) {
            System.out.println(task);
            for (Epic epic : epics.values()) {
                System.out.println(epic);
                for (Subtask subtask : getEpicSubTask(epic)) {
                    System.out.println(subtask);
                }
            }
        }
    }

    @Override
    public void printEpic(String nameEpic) {
        for (Epic epic : epics.values()) {
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
        if (tasks != null) {
            tasks.clear();
            prioritizedTasks.clear();
        } else {
            System.out.println("Список задач пуст");
        }
        if (epics != null) {
            epics.clear();
        } else {
            System.out.println("Список эпиков пуст");
        }
        if (subtasks != null) {
            subtasks.clear();
        } else {
            System.out.println("Список подзадач пуст");
        }
    }

    @Override
    public Task getTaskForId(int id) {
        if (id < tasks.size() && id >= 0) {
            Task task = tasks.get(id);
            historyManager.add(task);
            return task;
        } else {
            System.out.println("Неверно введен номер задачи");
            return null;
        }
    }

    @Override
    public Epic getEpicForId(int id) {
        if (id < epics.size() && id >= 0) {
            Epic epic = epics.get(id);
            historyManager.add(epic);
            return epic;
        } else {
            System.out.println("Неверно введен номер эпика");
            return null;
        }
    }

    @Override
    public Subtask getSubtaskForId(int id) {
        if (id < subtasks.size() && id >= 0) {
            Subtask subtask = subtasks.get(id);
            historyManager.add(subtask);
            return subtask;
        } else {
            System.out.println("Неверно введен номер подзадачи");
            return null;
        }
    }

    @Override
    public void deleteTaskForId(int id) {
        if (tasks.containsKey(id)) {
            prioritizedTasks.remove(tasks.get(id));
            tasks.remove(id);
            historyManager.remove(id);
            System.out.println("Задача с номером id='" + id + '\'' + " удалена.");
        } else {
            System.out.println("Такого номера задачи нет");
        }
    }

    @Override
    public void deleteEpicForId(int id) {
        if (epics.containsKey(id)) {
            prioritizedTasks.remove(epics.get(id));
            epics.remove(id);
            historyManager.remove(id);
            System.out.println("Эпик с номером id='" + id + '\'' + " удален.");
        } else {
            System.out.println("Такого номера эпика нет");
        }
    }

    @Override
    public void deleteSubTaskForId(int id) {
        if (subtasks.containsKey(id)) {
            prioritizedTasks.remove(subtasks.get(id));
            subtasks.remove(id);
            historyManager.remove(id);
            System.out.println("Подзадача с номером id='" + id + '\'' + " удалена.");
        } else {
            System.out.println("Такого номера подзадачи нет");
        }
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public Map<Integer, Epic> getEpics() {
        return epics;
    }
}
