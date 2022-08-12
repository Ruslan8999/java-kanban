package managers;

import ru.yandex.managers.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }
}
