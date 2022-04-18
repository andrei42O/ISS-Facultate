package service;

import model.User;
import persistance.IRepositoryDBUsers;

import java.util.Objects;

public class ServiceSecurity {
    IRepositoryDBUsers repositoryUsers = null;

    public ServiceSecurity(IRepositoryDBUsers repositoryUsers) {
        this.repositoryUsers = repositoryUsers;
    }

    public ServiceSecurity() {
    }

    public User login(String username, String password) throws RuntimeException{
        User user = repositoryUsers.findByUsername(username);
        if(user == null)
            throw new RuntimeException("No user found with that username!\n");
        if(Objects.equals(user.getPassword(), password)){
            return user;
        }
        throw new RuntimeException("Wrong password!\n");
    }

    public void setRepositoryUsers(IRepositoryDBUsers repositoryUsers) {
        this.repositoryUsers = repositoryUsers;
    }
}
