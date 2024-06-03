package services.generic;

import dao.CommonClass;
import dao.generic.IGenericDAO;
import exception.DAOException;
import exception.KeyTypeNotFoundException;
import exception.MoreRegisterException;
import exception.TableException;

import java.io.Serializable;
import java.util.Collection;

public abstract class GenericService <T extends CommonClass, E extends Serializable>
        implements IGenericService <T, E> {

    protected IGenericDAO <T, E> dao;
    public GenericService(IGenericDAO<T, E> dao) {
        this.dao = dao;
    }

    @Override
    public Boolean register(T entity) throws KeyTypeNotFoundException, DAOException {
        return this.dao.register(entity);
    }

    @Override
    public void delete (E value) throws DAOException {
        this.dao.delete(value);
    }

    @Override
    public void change(T entity) throws KeyTypeNotFoundException, DAOException {
        this.dao.change(entity);
    }

    @Override
    public T consult(E value) throws DAOException {
        try {
            return this.dao.consult(value);
        } catch (MoreRegisterException | TableException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collection<T> toFindAll() throws DAOException {
        return this.dao.toFindAll();
    }
}
