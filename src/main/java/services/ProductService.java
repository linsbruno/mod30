package services;

import dao.IProductDAO;
import domain.Product;
import services.generic.GenericService;

public class ProductService extends GenericService <Product, String> implements IProductService {

    public ProductService (IProductDAO dao) {
        super(dao);
    }
}
