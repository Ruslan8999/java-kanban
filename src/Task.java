public class Task {

    protected String nameTask;
    protected String descriptionTask;
    protected int id;
    protected TaskStatus statusTask;

    public Task(String nameTask, String descriptionTask, int identificationNumber, TaskStatus statusTask) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.id = identificationNumber;
        this.statusTask = statusTask;
    }

    public Task(String nameTask, String descriptionTask, int identificationNumber) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.id = identificationNumber;
        this.statusTask = TaskStatus.NEW;
    }

    public Task() {
    }

    public String getNameTask() {

        return nameTask;
    }

    public void setNameTask(String nameTask) {

        this.nameTask = nameTask;
    }

    public String getDescriptionTask() {

        return descriptionTask;
    }

    public void setDescriptionTask(String descriptionTask) {

        this.descriptionTask = descriptionTask;
    }

    public int getId() {

        return id;
    }

    public void setId(int identificationNumber) {

        this.id = identificationNumber;
    }

    public TaskStatus getStatusTask() {

        return statusTask;
    }

    @Override
    public String toString() {
        return "Task{" +
                "nameTask='" + nameTask + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", id=" + id +
                ", statusTask=" + statusTask +
                '}';
    }
}
