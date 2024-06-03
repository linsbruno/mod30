package dao;

import domain.Client;
import domain.Product;
import exception.DAOException;
import exception.KeyTypeNotFoundException;
import exception.MoreRegisterException;
import exception.TableException;

import java.util.Collection;

public abstract class ProductDAOMock implements IProductDAO {
    @Override
    public Boolean register(Product entity) throws KeyTypeNotFoundException {
        return null;
    }

    @Override
    public void delete(String value) { }

    @Override
    public void change(Product entity) throws KeyTypeNotFoundException { }

    @Override
    public Product consult(String value) {
        Product product = new Product();
        product.setCode(value);
        return product;
    }

    @Override
    public Collection<Product> toFindAll() {
        return null;
    }
}