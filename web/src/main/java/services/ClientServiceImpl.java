package services;


import dao.ClientRepositoryImpl;
import model.Client;
import model.enums_utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;


/**
 * Created by ilya on 20.08.2016.
 * Implementation of cliebt service
 */
@Service
public class ClientServiceImpl {

    @Autowired
    private ClientRepositoryImpl repository;

    /**
     *  Get Client entity from db by Id
     * @param id Id
     * @return  Client entity
     */
    public Client getClient(int id) {
        return repository.getClient(id);
    }

    /**
     *
     * @return List of all Client entities from db
     */
    public List<Client> getAll() {
        return repository.getAll();
    }

    /**
     *  Tries to remove Client entity from db
     * @param id Id
     * @return true if removed, otherwise false
     */
    @Transactional
    public boolean deleteClient(int id) {
        repository.deleteClient(id);
        return true;
    }

//    public boolean updateClient(Client client) {
//        return repository.save(client);
//    }

    /**
     * password Authentication
     * @param mail Email of Client r
     * @param pass Client's Password
     * @return Client entity, if found by email and password correct, null otherwise
     */
    public Client logIn(String mail,String pass){
        Client client = repository.getByEmail(mail);
        if(client == null)return null;
       return BCrypt.checkpw(pass,client.getPassword()) ? client : null;
    }


    public boolean checkPasswOnChange(Client loggedClient, String pass) {
        return BCrypt.checkpw(pass,loggedClient.getPassword());
    }


    @Transactional
    public void updateChangePassOrMerge(Client client) {
        String hashed = BCrypt.hashpw(client.getPassword(), BCrypt.gensalt());
        client.setPassword(hashed);
        repository.save(client);
    }

    /**
     * Redacts Client entity with or without password, or adds to db if Client is a new one
     * @param client Client entity
     * @return true on success
     */
    @Transactional
    public Client addClient(Client client) {
        if (client.getId() != 0) {
            Client old = repository.getClient(client.getId());
            client.setPassword(old.getPassword());
            client.setRegistered(old.getRegistered());
        } else {
            String hashed = BCrypt.hashpw(client.getPassword(), BCrypt.gensalt());
            client.setPassword(hashed);
            client.setRoles(new HashSet<Role>(Collections.singleton(Role.ROLE_USER)));
        }
        repository.save(client);
        return client;
    }
    /**
     *
     * @param mail Client's email
     * @return Client entity, or null
     */
    public Client getByEmail(String mail) {
        return  repository.getByEmail(mail);
    }
}
