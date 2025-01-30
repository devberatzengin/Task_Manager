public class Task {

    private static int idCounter = 1;
    private int taskId;
    private String taskName;
    private String taskDescription;
    private int taskStatus;
    private boolean isCompleted;

    public Task(int taskId, String taskName, String taskDescription, int taskStatus) {
        this.taskId = idCounter++;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStatus = taskStatus;
        this.isCompleted = (taskStatus == 100);
    }

    public int getTaskId() { return taskId; }
    public String getTaskName() { return taskName; }
    public String getTaskDescription() { return taskDescription; }
    public int getTaskStatus() { return taskStatus; }
    public boolean isCompleted() { return isCompleted; }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
        this.isCompleted = (taskStatus == 100);
    }

    @Override
    public String toString() {
        return taskId + ";" + taskName + ";" + taskDescription + ";" + taskStatus + "%" + ";" + (isCompleted ? "Completed" : "Not Completed");
    }
}
