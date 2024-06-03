package dao;

import domain.Client;
import exception.KeyTypeNotFoundException;

import java.util.Collection;

public class ClientDAOMock implements IClientDAO {
    @Override
    public Boolean register(Client entity) throws KeyTypeNotFoundException {
        return true;
    }

    @Override
    public void delete(Long value) {}

    @Override
    public void change(Client entity) throws KeyTypeNotFoundException {}

    @Override
    public Client consult(Long value) {
        Client client = new Client();
        client.setCode(value);
        return client;
    }

    @Override
    public Collection<Client> toFindAll() {
        return null;
    }
}
