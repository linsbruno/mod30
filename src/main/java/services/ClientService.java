package services;

import dao.IClientDAO;
import dao.generic.IGenericDAO;
import domain.Client;
import exception.DAOException;
import exception.MoreRegisterException;
import exception.TableException;
import services.generic.GenericService;

public class ClientService extends GenericService <Client, Long> implements IClientService {

    public ClientService(IClientDAO clientDAO) {
        super((IGenericDAO<Client, Long>) clientDAO);
    }
    @Override
    public Client toFindByCode(Long code) throws DAOException {
        try {
            return this.dao.consult(code);
        } catch (MoreRegisterException | TableException e) {
            e.printStackTrace();
        }
        return null;
    }
}