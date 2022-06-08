public class Epic extends Task {
    public Epic(String nameTask, String descriptionTask, int identificationNumber){
        super(nameTask, descriptionTask, identificationNumber);
    }

    public void setStatusTask(TaskStatus taskStatus){
        this.statusTask=taskStatus;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "nameEpic='" + nameTask + '\'' +
                ", descriptionEpic='" + descriptionTask + '\'' +
                ", id=" + id +
                ", statusEpic=" + statusTask +
                '}';
    }
}
