package model;

public class Admin extends User{
    public Admin(String username, String password, String name) {
        super(username, password, name, JobType.Admin);
    }

    public Admin() {
    }

    public Admin(User user) {
        super(user.getUsername(), user.getPassword(), user.getName(),JobType.Agent);
    }
}
