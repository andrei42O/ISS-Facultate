package model;

import java.io.Serializable;
import java.util.Objects;

public class User extends Entity<Long> {
    private static final long serialVersionUID = 6529685098267751237L;
    private String username;
    private String password;
    private JobType job;
    private String name;

    public User(String username, String password, String name, JobType job) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) && job == user.job && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, password, job, name);
    }

    @Override
    public String toString() {
        return  username + '\'' +
                ", " + password + '\'' +
                ", " + job;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public JobType getJob() {
        return job;
    }

    public void setJob(JobType job) {
        this.job = job;
    }

}
