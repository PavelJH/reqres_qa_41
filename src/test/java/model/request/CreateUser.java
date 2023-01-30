package model.request;

public class CreateUser { // простой старый Поджоб обьект джава, без всяких зависимотей. Класс из которого получается, простой обьект
    private String name;
    private String job;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public CreateUser(String name, String job) {
        this.name = name;
        this.job = job;
    }
}
