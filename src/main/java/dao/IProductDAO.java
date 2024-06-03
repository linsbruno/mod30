package dao;

import dao.generic.IGenericDAO;
import domain.Product;

public interface IProductDAO extends IGenericDAO<Product, String> {
    Class<Product> getClassType();

    void updateData(Product entity, Product entityRegistered);
}