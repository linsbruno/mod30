package services.generic;

import dao.CommonClass;
import exception.DAOException;
import exception.KeyTypeNotFoundException;

import java.io.Serializable;
import java.util.Collection;

public interface IGenericService <T extends CommonClass, E extends Serializable> {

    public Boolean register(T entity) throws KeyTypeNotFoundException, DAOException;

    public void delete (E value) throws DAOException;

    public void change(T entity) throws KeyTypeNotFoundException, DAOException;

    public T consult(E value) throws DAOException;

    public Collection<T> toFindAll() throws DAOException;
}