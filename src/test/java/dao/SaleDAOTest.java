package dao;

import dao.generic.jdbc.ConnectionFactory;
import domain.Client;
import domain.Product;
import domain.Sale;
import exception.DAOException;
import exception.KeyTypeNotFoundException;
import exception.MoreRegisterException;
import exception.TableException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;

public class SaleDAOTest {

    private ISaleDAO saleDAO;
    private IClientDAO clientDAO;
    private IProductDAO productDAO;
    private Client client;
    private Product product;

    public SaleDAOTest() {
        saleDAO = new SaleDAO();
        clientDAO = new ClientDAO();
        productDAO = new ProductDAO();
    }

    @Before
    public void init() throws KeyTypeNotFoundException, MoreRegisterException, TableException, DAOException {
        this.client = createClient();
        this.product = createProduct("A1", BigDecimal.TEN);
    }

    @After
    public void end() throws DAOException {
        deleteSale();
        deleteProduct();
        clientDAO.delete(this.client.getCode());
    }

    private void deleteProduct() throws DAOException {
        Collection <Product> list = this.productDAO.toFindAll();
        for (Product prod : list) {
            this.productDAO.delete(prod.getCode());
        }
    }

    private Sale createSale(String code) {
        Sale sale = new Sale();
        sale.setCode(code);
        sale.setSaleDate(Instant.now());
        sale.setClient(this.client);
        sale.setStatus(Sale.Status.INICIADA);
        sale.toAddProduct(this.product, 2);
        return sale;
    }

    private Client createClient() throws KeyTypeNotFoundException, DAOException {
        Client client = new Client();
        client.setCode(12312312312L);
        client.setName("Bruno");
        client.setGender("Homem");
        client.setPhone(1199999999L);
        client.setHomeAddress("Rua JK, 999");
        client.setCity("Campinas");
        client.setState("SP");

        clientDAO.register(client);
        return client;
    }

    private Product createProduct(String code, BigDecimal value) throws KeyTypeNotFoundException, MoreRegisterException, TableException, DAOException {
        Product product = new Product();
        product.setCode(code);
        product.setName("Produto 1");
        product.setDescription("Produto 1");
        product.setValue(value);

        productDAO.register(product);
        return product;
    }

    private void deleteSale() throws DAOException {
        String sqlProd = "DELETE FROM TB_PRODUCT_UNIT";
        executeDelete(sqlProd);

        String sqlSale = "DELETE FROM TB_SALE";
        executeDelete(sqlSale);
    }

    private void executeDelete(String sql) throws DAOException {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;

        try {
            connection = getConnection();
            stm = connection.prepareStatement(sql);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("ERRO EXLUINDO OBJETO ", e);
        } finally {
            closeConnection(connection, stm, null);
        }
    }

    protected void closeConnection(Connection connection, PreparedStatement stm, ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
            if (connection != null) {
                assert stm != null;
                if (!stm.isClosed()) {
                    connection.close();
                }
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    protected Connection getConnection() throws DAOException {
        try {
            return ConnectionFactory.getConnection();
        } catch (SQLException e) {
            throw new DAOException("ERRO ABRINDO CONEXAO COM BANCO DE DADOS ", e);
        }
    }


    @Test
    public void search() throws KeyTypeNotFoundException, MoreRegisterException, TableException, DAOException {
        Sale sale = createSale("A1");
        Boolean retrn = saleDAO.register(sale);
        Assert.assertTrue(retrn);

        Sale saleConsulted = saleDAO.consult(sale.getCode());
        Assert.assertNotNull(saleConsulted);
        Assert.assertEquals(sale.getCode(), saleConsulted.getCode());
    }

    @Test
    public void save() throws KeyTypeNotFoundException, MoreRegisterException, TableException, DAOException {
        Sale sale = createSale("A2");
        Boolean retrn = saleDAO.register(sale);
        Assert.assertTrue(retrn);

        Assert.assertTrue(sale.getTotalValue().equals(BigDecimal.valueOf(20)));
        Assert.assertTrue(sale.getStatus().equals(Sale.Status.INICIADA));

        Sale saleConsulted = saleDAO.consult(sale.getCode());
        Assert.assertTrue(saleConsulted.getId() != null);
        Assert.assertEquals(sale.getCode(), saleConsulted.getCode());
    }

    @Test
    public void cancelSale() throws KeyTypeNotFoundException, MoreRegisterException, TableException, DAOException {
        String codeSale = "A3";
        Sale sale = createSale(codeSale);
        Boolean retrn = saleDAO.register(sale);
        Assert.assertTrue(retrn);
        Assert.assertNotNull(sale);
        Assert.assertEquals(codeSale, sale.getCode());

        saleDAO.toCancelSale(sale);

        Sale saleConsulted = saleDAO.consult(sale.getCode());
        Assert.assertEquals(codeSale, saleConsulted.getCode());
        Assert.assertEquals(Sale.Status.CANCELADA, saleConsulted.getStatus());
    }

    @Test
    public void addMoreSameProducts() throws KeyTypeNotFoundException, MoreRegisterException, TableException, DAOException {
        String codeSale = "A4";
        Sale sale = createSale(codeSale);
        Boolean retrn = saleDAO.register(sale);
        Assert.assertTrue(retrn);
        Assert.assertNotNull(sale);
        Assert.assertEquals(codeSale, sale.getCode());

        Sale saleConsulted = saleDAO.consult(sale.getCode());
        saleConsulted.toAddProduct(product, 1);

        Assert.assertTrue(saleConsulted.getTotalProductsQtd() == 3);
        BigDecimal valueTotal = BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(saleConsulted.getTotalValue().equals(valueTotal));
        Assert.assertTrue(saleConsulted.getStatus().equals(Sale.Status.INICIADA));
    }

    @Test
    public void addMoreDifferentProducts() throws KeyTypeNotFoundException, MoreRegisterException, TableException, DAOException {
        String codeSale = "A5";
        Sale sale = createSale(codeSale);
        Boolean retrn = saleDAO.register(sale);
        Assert.assertTrue(retrn);
        Assert.assertNotNull(sale);
        Assert.assertEquals(codeSale, sale.getCode());

        Product prod = createProduct(codeSale, BigDecimal.valueOf(50));
        Assert.assertNotNull(prod);
        Assert.assertEquals(codeSale, prod.getCode());

        Sale saleConsulted = saleDAO.consult(sale.getCode());
        saleConsulted.toAddProduct(product, 1);

        Assert.assertTrue(saleConsulted.getTotalProductsQtd() == 3);
        BigDecimal valueTotal = BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(saleConsulted.getTotalValue().equals(valueTotal));
        Assert.assertTrue(saleConsulted.getStatus().equals(Sale.Status.INICIADA));
    }

    @Test(expected = DAOException.class)
    public void saveSaleExistingCode() throws KeyTypeNotFoundException, DAOException {
        Sale sale = createSale("A6");
        Boolean retrn = saleDAO.register(sale);
        Assert.assertTrue(retrn);

        Boolean retrn1 = saleDAO.register(sale);
        Assert.assertFalse(retrn1);
        Assert.assertEquals(sale.getStatus(), Sale.Status.INICIADA);
    }

    @Test
    public void removeProduct() throws KeyTypeNotFoundException, MoreRegisterException, TableException, DAOException {
        String codeSale = "A7";
        Sale sale = createSale(codeSale);
        Boolean retrn = saleDAO.register(sale);
        Assert.assertTrue(retrn);
        Assert.assertNotNull(sale);
        Assert.assertEquals(codeSale, sale.getCode());

        Product prod = createProduct(codeSale, BigDecimal.valueOf(50));
        Assert.assertNotNull(prod);
        Assert.assertEquals(codeSale, prod.getCode());

        Sale saleConsulted = saleDAO.consult(codeSale);
        saleConsulted.toAddProduct(prod, 1);
        Assert.assertTrue(saleConsulted.getTotalProductsQtd() == 3);
        BigDecimal valueTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(saleConsulted.getTotalValue().equals(valueTotal));

        saleConsulted.toRemoveProduct(prod, 1);
        Assert.assertTrue(saleConsulted.getTotalProductsQtd() == 2);
        valueTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(saleConsulted.getTotalValue().equals(valueTotal));
        Assert.assertTrue(saleConsulted.getStatus().equals(Sale.Status.INICIADA));
    }

    @Test
    public void removeOnlyOneProduct() throws KeyTypeNotFoundException, MoreRegisterException, TableException, DAOException {
        String codeSale = "A8";
        Sale sale = createSale(codeSale);
        Boolean retrn = saleDAO.register(sale);
        Assert.assertTrue(retrn);
        Assert.assertNotNull(sale);
        Assert.assertEquals(codeSale, sale.getCode());

        Product prod = createProduct(codeSale, BigDecimal.valueOf(50));
        Assert.assertNotNull(prod);
        Assert.assertEquals(codeSale, prod.getCode());

        Sale saleConsulted = saleDAO.consult(codeSale);
        saleConsulted.toAddProduct(prod, 1);
        Assert.assertTrue(saleConsulted.getTotalProductsQtd() == 3);
        BigDecimal valueTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(saleConsulted.getTotalValue().equals(valueTotal));

        saleConsulted.toRemoveProduct(prod, 1);
        Assert.assertTrue(saleConsulted.getTotalProductsQtd() == 2);
        valueTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(saleConsulted.getTotalValue().equals(valueTotal));
        Assert.assertTrue(saleConsulted.getStatus().equals(Sale.Status.INICIADA));
    }

    @Test
    public void removeAllProduct() throws KeyTypeNotFoundException, MoreRegisterException, TableException, DAOException {
        String codeSale = "A9";
        Sale sale = createSale(codeSale);
        Boolean retrn = saleDAO.register(sale);
        Assert.assertTrue(retrn);
        Assert.assertNotNull(sale);
        Assert.assertEquals(codeSale, sale.getCode());

        Product prod = createProduct(codeSale, BigDecimal.valueOf(50));
        Assert.assertNotNull(prod);
        Assert.assertEquals(codeSale, prod.getCode());

        Sale saleConsulted = saleDAO.consult(codeSale);
        saleConsulted.toAddProduct(prod, 1);
        Assert.assertTrue(saleConsulted.getTotalProductsQtd() == 3);
        BigDecimal valueTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(saleConsulted.getTotalValue().equals(valueTotal));

        saleConsulted.toRemoveAll();
        Assert.assertTrue(saleConsulted.getTotalProductsQtd() == 0);
        Assert.assertTrue(saleConsulted.getTotalValue().equals(BigDecimal.valueOf(0)));
        Assert.assertTrue(saleConsulted.getStatus().equals(Sale.Status.INICIADA));
    }

    @Test
    public void endingSale() throws KeyTypeNotFoundException, MoreRegisterException, TableException, DAOException {
        String codeSale = "A10";
        Sale sale = createSale(codeSale);
        Boolean retrn = saleDAO.register(sale);
        Assert.assertTrue(retrn);
        Assert.assertNotNull(sale);
        Assert.assertEquals(codeSale, sale.getCode());

        saleDAO.toFinishSale(sale);
        Sale saleConsulted = saleDAO.consult(codeSale);
        Assert.assertEquals(sale.getCode(), saleConsulted.getCode());
        Assert.assertEquals(Sale.Status.CONCLUIDA, saleConsulted.getStatus());
    }

    @Test (expected = UnsupportedOperationException.class)
    public void tryAddProductsACompletedSale() throws KeyTypeNotFoundException, MoreRegisterException, TableException, DAOException {
        String codeSale = "A11";
        Sale sale = createSale(codeSale);
        Boolean retrn = saleDAO.register(sale);
        Assert.assertTrue(retrn);
        Assert.assertNotNull(sale);
        Assert.assertEquals(codeSale, sale.getCode());

        saleDAO.toFinishSale(sale);
        Sale saleConsulted = saleDAO.consult(codeSale);
        Assert.assertEquals(sale.getCode(), saleConsulted.getCode());
        Assert.assertEquals(Sale.Status.CONCLUIDA, saleConsulted.getStatus());

        saleConsulted.toAddProduct(this.product, 1);
    }

}
