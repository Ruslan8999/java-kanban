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


        //Получение по идентификатору:
        /*System.out.println(manager.getTaskForId(0));
        System.out.println(manager.getEpicForId(0));
        System.out.println(manager.getSubtaskForId(0));*/

        //Удаление по идентификатору:
        /*manager.deleteTaskForId(0);
        //manager.deleteEpicForId(0);
        //manager.deleteSubTaskForId(1);*/

        //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра:
        /*ru.yandex.Task.Task newTask =new ru.yandex.Task.Task("Новая задача", "Новая цель",0);
        manager.updateTask(newTask);
        ru.yandex.ru.yandex.Task.Task.Epic.Epic newEpic1 = new ru.yandex.ru.yandex.Task.Task.Epic.Epic("new Работа", "new Доделать проект",0);
        manager.updateTask(newEpic1);
        ru.yandex.Task.Subtusk.Subtask newSubtask1 = new ru.yandex.Task.Subtusk.Subtask("new Коммит", "new Закоммитить результат", 0, epic1);
        manager.updateTask(newSubtask1);

        System.out.println(manager.getTaskForId(0));
        System.out.println(manager.getEpicForId(0));
        System.out.println(manager.getSubtaskForId(0));*/


        //Удаление всех задач:
        //manager.deleteAll();

        //Получение списка всех подзадач определённого эпика:
        /*manager.printEpic("Работа");*/

    }
}
