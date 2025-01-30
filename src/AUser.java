public abstract class AUser {


    abstract void register();
    abstract boolean login();


    protected TaskManager taskManager;
    public TaskManager getTaskManager() {
        return taskManager;
    }

    //void logout();

    abstract void changePassword();
    abstract void changeEmail();
    abstract void changeUserName();
}
