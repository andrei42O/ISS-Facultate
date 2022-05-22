package service;

import model.User;
import persistance.IRepositoryDBUsers;

public class ServiceAdmin implements IService{
    private IRepositoryDBUsers repositoryUsers = null;

    public ServiceAdmin(IRepositoryDBUsers repositoryAgent) {
        this.repositoryUsers = repositoryAgent;
    }

    public ServiceAdmin() {
    }

    public void setRepositoryUsers(IRepositoryDBUsers repositoryUsers) {
        this.repositoryUsers = repositoryUsers;
    }

    public User addUser(User agent) throws RuntimeException{
        User _agent = repositoryUsers.save(agent);
        if(_agent == null)
            throw new RuntimeException("The agent couldn't be saved!\n");
        return _agent;
    }

    public User updateAgent(User newAgent, Long id){
        User _agent = repositoryUsers.update(newAgent, id);
        System.out.println(_agent);
        if(_agent == null)
            throw new RuntimeException("An error occurred!\nThe agent was not updated!\n");
        return _agent;
    }

    public User deleteAgent(User agent){
        User _agent = repositoryUsers.delete(agent);
        if(_agent == null)
            throw new RuntimeException("An error occurred!\n The agent could not be deleted!");
        return _agent;
    }

    public Iterable<User> getAgents(){
        return repositoryUsers.getAll();
    }
}
