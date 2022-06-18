package ru.yandex;

import ru.yandex.manager.Manager;
import ru.yandex.task.Epic;
import ru.yandex.task.Subtask;
import ru.yandex.task.Task;
import ru.yandex.task.TaskStatus;

public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();

        //Создание. Сам объект должен передаваться в качестве параметра:
        Task task1 = new Task("Трекер задач", "Научиться делать трекер");
        Epic epic1 = new Epic("Работа", "Доделать проект");
        Subtask subtask1 = new Subtask("Коммит", "Закоммитить результат", epic1);
        Subtask subtask2 = new Subtask("Конфлюенс", "Сделать заметки в конфлюенс", epic1);
        Epic epic2 = new Epic("Переезд", "Подготовиться к переезду");
        Subtask subtask3 = new Subtask("Собрать вещи", "Разложить все по коробкам", epic2);
        manager.addNewTask(task1);
        manager.addNewTask(epic1);
        manager.addNewTask(subtask1);
        manager.addNewTask(subtask2);
        manager.addNewTask(epic2);
        manager.addNewTask(subtask3);

        //Получение списка всех задач:
        manager.printAll();

        System.out.println("================================");

        //Изменение статусов подзадач и в следствии изменение статуса эпика:
        subtask1 = new Subtask("Коммит", "Закоммитить результат", subtask1.getId(), epic1, TaskStatus.IN_PROGRESS);
        manager.updateTask(subtask1);
        manager.printAll();
        System.out.println("================================");

        subtask1 = new Subtask("Коммит", "Закоммитить результат", subtask1.getId(), epic1, TaskStatus.DONE);
        manager.updateTask(subtask1);
        manager.printAll();
        System.out.println("================================");

        subtask2 = new Subtask("Конфлюенс", "Сделать заметки в конфлюенс", subtask2.getId(), epic1, TaskStatus.DONE);
        manager.updateTask(subtask2);
        manager.printAll();
        System.out.println("================================");

        subtask3 = new Subtask("Доработки", "Внести исправления",  epic1);
        manager.addNewTask(subtask3);
        manager.printAll();
        System.out.println("================================");
    }
}
