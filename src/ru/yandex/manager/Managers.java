package ru.yandex.manager;

import ru.yandex.manager.memory.InMemoryHistoryManager;
import ru.yandex.manager.memory.InMemoryTaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
