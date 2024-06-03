package dao;

import dao.generic.GenericDAO;
import domain.Product;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductDAO extends GenericDAO <Product, String> implements IProductDAO {

    public ProductDAO() {
        super();
    }
    @Override
    public Class<Product> getClassType() {
        return Product.class;
    }

    @Override
    public void updateData(Product entity, Product entityRegistered) {
        entityRegistered.setCode(entity.getCode());
        entityRegistered.setDescription(entity.getDescription());
        entityRegistered.setName(entity.getName());
        entityRegistered.setValue(entity.getValue());
        entityRegistered.setUnit(entity.getUnit());
    }

    @Override
    protected String getQueryInsert() {
        return "INSERT INTO TB_PRODUCT" +
                "(ID, CODE, NAME, DESCRIPTION, VALUE, UNITS)" +
                "VALUES (nextval('sq_product'),?,?,?,?,?)";
    }

    @Override
    protected String getQueryDelete() {
        return null;
    }

    @Override
    protected String getQueryUpdate() {
        return "UPDATE TB_PRODUCT" +
                "SET CODE = ?, NOME = ?, DESCRIPTION = ?, VALUE = ?, UNITS = ? " +
                "WHERE CODE = ?";
    }

    @Override
    protected void setParamsQueryInsert(PreparedStatement stmInsert, Product entity) throws SQLException {
        stmInsert.setLong(1, entity.getId());
        stmInsert.setString(2, entity.getName());
        stmInsert.setString(3, entity.getDescription());
        stmInsert.setBigDecimal(4, entity.getValue());
        stmInsert.setString(5, entity.getUnit());
    }

    @Override
    protected void setParamsQueryDelete(PreparedStatement stmDelete, String value) throws SQLException {

    }

    @Override
    protected void setParamsQueryUpdate(PreparedStatement stmUpdate, Product entity) throws SQLException {
        stmUpdate.setLong(1, entity.getId());
        stmUpdate.setString(2, entity.getName());
        stmUpdate.setString(3, entity.getDescription());
        stmUpdate.setBigDecimal(4, entity.getValue());
        stmUpdate.setString(5, entity.getUnit());
    }

    @Override
    protected void setParamsQuerySelect(PreparedStatement stmSelect, String value) throws SQLException {
        stmSelect.setString(1, value);
    }
}
