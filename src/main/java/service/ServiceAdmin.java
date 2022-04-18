package service;

import model.User;
import persistance.IRepositoryDBUsers;

public class ServiceAdmin {
    private IRepositoryDBUsers repositoryAgent = null;

    public ServiceAdmin(IRepositoryDBUsers repositoryAgent) {
        this.repositoryAgent = repositoryAgent;
    }

    public ServiceAdmin() {
    }

    public void setRepositoryAgent(IRepositoryDBUsers repositoryAgent) {
        this.repositoryAgent = repositoryAgent;
    }

    public User addAgent(User agent) throws RuntimeException{
        User _agent = repositoryAgent.save(agent);
        if(_agent == null)
            throw new RuntimeException("The agent couldn't be saved!\n");
        return _agent;
    }

    public User updateAgent(User newAgent, Long id){
        User _agent = repositoryAgent.update(newAgent, id);
        System.out.println(_agent);
        if(_agent == null)
            throw new RuntimeException("An error occurred!\nThe agent was not updated!\n");
        return _agent;
    }

    public User deleteAgent(User agent){
        User _agent = repositoryAgent.delete(agent);
        if(_agent == null)
            throw new RuntimeException("An error occurred!\n The agent could not be deleted!");
        return _agent;
    }

    public Iterable<User> getAgents(){
        return repositoryAgent.getAll();
    }
}
