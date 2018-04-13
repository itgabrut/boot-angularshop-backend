package dao;


import model.Client;

import java.util.List;

/**
 * Created by ilya on 20.08.2016.
 * repo
 */
public interface ClientRepository {

    Client getClient(int clientId);

    void deleteClient(int clientId);

    List<Client> getAll();

    Client save(Client client);

    Client getByEmail(String mail);
}
