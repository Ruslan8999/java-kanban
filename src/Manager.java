import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    private int taskCounter;
    private int subTaskCounter;
    private int epicTaskCounter;

    private HashMap<Integer, Task> taskHashMap;
    private HashMap<Integer, Subtask> subtaskHashMap;
    private HashMap<Integer, Epic> epicHashMap;

    public Manager() {

        taskCounter = 0;
        subTaskCounter = 0;
        epicTaskCounter = 0;
        taskHashMap = new HashMap<>();
        subtaskHashMap = new HashMap<>();
        epicHashMap = new HashMap<>();
    }

    public int getTaskNum() {

        return taskCounter++;
    }

    public int getSubTaskNum() {

        return subTaskCounter++;
    }

    public int getEpicTaskNum() {

        return epicTaskCounter++;
    }

    public void addNewTask(Task task) {
        if (task == null) {
            System.out.println("Error! task = null");
            return;
        }
        if (task instanceof Epic) {
            epicHashMap.put(task.getId(), (Epic) task);
        } else if (task instanceof Subtask) {
            subtaskHashMap.put(task.getId(), (Subtask) task);
        } else {
            taskHashMap.put(task.getId(), task);
        }
    }

    public void updateTask(Task task) {
        if (task == null) {
            System.out.println("Error! task = null");
            return;
        }
        if (task instanceof Epic) {
            epicHashMap.put(task.getId(), (Epic) task);
        } else if (task instanceof Subtask) {
            if(subtaskHashMap.containsKey(task.getId())) {
                TaskStatus oldTaskStatus = subtaskHashMap.get(task.getId()).getStatusTask();
                subtaskHashMap.put(task.getId(), (Subtask) task);
                if(!oldTaskStatus.equals(task.getStatusTask())){
                    updateEpicStatus(((Subtask) task).getParent());
                }
            } else {
                System.out.println("Manager.updateTask:Error");
            }
        } else {
            taskHashMap.put(task.getId(), task);
        }
    }

    public void updateEpicStatus(Epic epic){
        if(epic==null){
            System.out.println("Error");
            return;
        }
        ArrayList<Subtask> subtasks = getEpicSubTask(epic);
        if (subtasks.isEmpty()){
            epic.setStatusTask(TaskStatus.NEW);
            return;
        }
        int newStatusCounter = 0;
        int doneStatusCounter = 0;
        for(Subtask subtask : subtasks){
            switch (subtask.getStatusTask()){
                case NEW: newStatusCounter++; break;
                case IN_PROGRESS: break;
                case DONE: doneStatusCounter++; break;
            }
            if (newStatusCounter==subtasks.size()){
                epic.setStatusTask(TaskStatus.NEW);
            } else if (doneStatusCounter==subtasks.size()) {
                epic.setStatusTask(TaskStatus.DONE);
            } else {
                epic.setStatusTask(TaskStatus.IN_PROGRESS);
            }
        }
    }

    public ArrayList<Subtask> getEpicSubTask(Epic epic) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
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

    public void printEpic(String nameEpic) {
        for (Epic epic : epicHashMap.values()) {
            if (nameEpic == epic.nameTask) {
                for (Subtask subtask : getEpicSubTask(epic)) {
                    System.out.println(subtask);
                }
            } else {
                System.out.println("Такого эпика нет");
            }
        }
    }

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

    public Task getTaskForId(int id) {
        if (id < taskHashMap.size() && id >= 0) {
            return taskHashMap.get(id);
        } else {
            System.out.println("Неверно введен номер задачи");
            return null;
        }
    }

    public Epic getEpicForId(int id) {
        if (id < epicHashMap.size() && id >= 0) {
            return epicHashMap.get(id);
        } else {
            System.out.println("Неверно введен номер эпика");
            return null;
        }
    }

    public Subtask getSubtaskForId(int id) {
        if (id < subtaskHashMap.size() && id >= 0) {
            return subtaskHashMap.get(id);
        } else {
            System.out.println("Неверно введен номер подзадачи");
            return null;
        }
    }

    public void deleteTaskForId(int id) {
        if (taskHashMap.containsKey(id)) {
            taskHashMap.remove(id);
            System.out.println("Задача с номером id='" + id + '\'' + " удалена.");
        } else {
            System.out.println("Такого номера задачи нет");
        }
    }

    public void deleteEpicForId(int id) {
        if (epicHashMap.containsKey(id)) {
            epicHashMap.remove(id);
            System.out.println("Эпик с номером id='" + id + '\'' + " удален.");
        } else {
            System.out.println("Такого номера эпика нет");
        }
    }

    public void deleteSubTaskForId(int id) {
        if (subtaskHashMap.containsKey(id)) {
            subtaskHashMap.remove(id);
            System.out.println("Подзадача с номером id='" + id + '\'' + " удалена.");
        } else {
            System.out.println("Такого номера подзадачи нет");
        }
    }


}
