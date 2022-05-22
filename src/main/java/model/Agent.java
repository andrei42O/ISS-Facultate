package model;

public class Agent extends User{
    public Agent(String username, String password, String name) {
        super(username, password, name, JobType.Agent);
    }

    public Agent() {
    }

    public Agent(User user){
        super(user.getUsername(), user.getPassword(), user.getName(),JobType.Agent);
    }
}
