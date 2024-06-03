import dao.IProductDAO;
import dao.ProductDAO;
import domain.Product;
import exception.DAOException;
import exception.KeyTypeNotFoundException;
import exception.MoreRegisterException;
import exception.TableException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collection;

public class ProductDAOTest {

    private IProductDAO productDAO;
    public ProductDAOTest() {
        productDAO = new ProductDAO();
    }

    @After
    public void end() throws DAOException {
        Collection<Product> list = productDAO.toFindAll();
        list.forEach(prod -> {
            try {
                productDAO.delete(prod.getCode());
            } catch (DAOException e) {
                e.printStackTrace();
            }
        });
    }

    private Product createProduct(String code) throws KeyTypeNotFoundException, DAOException {
        Product product = new Product();
        product.setCode(code);
        product.setName("Produto 1");
        product.setDescription("Produto 1");
        product.setValue(BigDecimal.TEN);
        product.setUnit("10");
        productDAO.register(product);
        return product;
    }

    private void delete(String value) throws DAOException {
        this.productDAO.delete(value);
    }

    @Test
    public void search() throws MoreRegisterException, TableException, KeyTypeNotFoundException, DAOException {
        Product product = createProduct("A1");
        Assert.assertNotNull(product);
        Product productDB = this.productDAO.consult(product.getCode());
        Assert.assertNotNull(productDB);
        delete(productDB.getCode());
    }

    @Test
    public void save() throws KeyTypeNotFoundException, DAOException {
        Product product = createProduct("A2");
        Assert.assertNotNull(product);
        delete(product.getCode());
    }

    @Test
    public void delete() throws MoreRegisterException, TableException, KeyTypeNotFoundException, DAOException {
        Product product = createProduct("A3");
        Assert.assertNotNull(product);
        delete(product.getCode());
        Product productDB = this.productDAO.consult(product.getCode());
        Assert.assertNull(productDB);
    }

    @Test
    public void updateClient() throws MoreRegisterException, TableException, KeyTypeNotFoundException, DAOException {
        Product product = createProduct("A4");
        product.setName("João Lima");
        productDAO.change(product);
        Product productDB = this.productDAO.consult(product.getCode());
        Assert.assertNotNull(productDB);
        Assert.assertEquals("João Lima", productDB.getName());

        delete(product.getCode());
        Product productDB1 = this.productDAO.consult(product.getCode());
        Assert.assertNull(productDB1);
    }

    @Test
    public void searchAll() throws KeyTypeNotFoundException, DAOException {
        createProduct("A5");
        createProduct("A6");

        Collection<Product> list = productDAO.toFindAll();
        Assert.assertNotNull(list);
        Assert.assertEquals(2, list.size());

        for (Product prod : list) {
            delete(prod.getCode());
        }

        list = productDAO.toFindAll();
        Assert.assertNotNull(list);
        Assert.assertEquals(0, list.size());
    }
}