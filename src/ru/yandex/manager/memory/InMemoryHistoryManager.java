package ru.yandex.manager.memory;

import ru.yandex.manager.HistoryManager;
import ru.yandex.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private final Map<Integer, Node> nodes;

    public InMemoryHistoryManager() {
        nodes = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (task == null ) {
            return;
        }
        final int id = task.getId();
        linkLast(task);
        nodes.put(id, tail);
    }

    private void linkLast(Task task) {
        final Node node = new Node(tail, task, null);
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
    }

    @Override
    public List<Task> getHistory() {
        return getTask();
    }

    private List<Task> getTask() {
        List<Task> result = new ArrayList<>();
        Node node = head;
        while (node != null) {
            result.add(node.getTask());
            node = node.next;
        }
        return result;
    }

    @Override
    public void remove(int id) {
        Node node = nodes.get(id);
        removeNode(node);
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }

        if (node.next != null && node.prev != null){
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        if (node.next == null && node.prev != null){
            tail = node.prev;
        }

        if (node.next != null && node.prev == null){
            head = node.next;
        }

        if (node.next == null && node.prev == null){
            head = tail;
        }
    }

    public static class Node {
        private Task task;
        private Node prev;
        private Node next;

        public Node(Node prev, Task task, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }

        public Task getTask() {
            return task;
        }

        public void setTask(Task task) {
            this.task = task;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }
}