package services;

import domain.Client;
import exception.DAOException;
import services.generic.IGenericService;

public interface IClientService extends IGenericService <Client, Long> {

    Client toFindByCode(Long code) throws DAOException;
}