package dao;

import dao.generic.IGenericDAO;
import domain.Sale;
import exception.DAOException;
import exception.KeyTypeNotFoundException;

public interface ISaleDAO extends IGenericDAO<Sale, String> {

    public void toFinishSale(Sale sale) throws KeyTypeNotFoundException, DAOException;
    public void toCancelSale(Sale sale) throws KeyTypeNotFoundException, DAOException;
}
