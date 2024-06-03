package dao;

import dao.generic.GenericDAO;
import domain.Sale;
import exception.DAOException;
import exception.KeyTypeNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SaleDAO extends GenericDAO<Sale, String> implements ISaleDAO {

    @Override
    public Class<Sale> getClassType() {
        return Sale.class;
    }

    @Override
    public void updateData(Sale entity, Sale entityRegistered) {
        entityRegistered.setCode(entity.getCode());
        entityRegistered.setStatus(entity.getStatus());
    }

    @Override
    public void delete(String value) {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    public void toFinishSale(Sale sale) throws KeyTypeNotFoundException, DAOException {
        Connection connection = null;
        PreparedStatement stm = null;

        try {
            String sql = "UPDATE TB_SALE SET SALE_STATUS = ? WHERE ID = ?";
            connection = getConnection();
            stm = connection.prepareStatement(sql);
            stm.setString(1, Sale.Status.CONCLUIDA.name());
            stm.setLong(2, sale.getId());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("ERRO ATUALIZANDO OBJETO ", e);
        } finally {
            closeConnection(connection, stm, null);
        }
    }

    @Override
    public void toCancelSale(Sale sale) throws KeyTypeNotFoundException, DAOException {
        Connection connection = null;
        PreparedStatement stm = null;

        try {
            String sql = "UPDATE TB_SALE SET SALE_STATUS = ? WHERE ID = ?";
            connection = getConnection();
            stm = connection.prepareStatement(sql);
            stm.setString(1, Sale.Status.CANCELADA.name());
            stm.setLong(2, sale.getId());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("ERRO ATUALIZANDO OBJETO ", e);
        } finally {
            closeConnection(connection, stm, null);
        }
    }


    @Override
    protected String getQueryInsert() {
        return "INSERT INTO TB_SALE" +
                "(ID, CODE, ID_CLIENT_FK, TOTAL_VALUE, SALE_DATE, SALE_STATUS)" +
                "VALUES (nextval('sq_sale'),?,?,?,?,?)";
    }

    @Override
    protected String getQueryDelete() {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    protected String getQueryUpdate() {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    protected void setParamsQueryInsert(PreparedStatement stmInsert, Sale entity) throws SQLException {
        stmInsert.setLong(1, entity.getId());
        stmInsert.setString(2, entity.getCode());
        stmInsert.setBigDecimal(3, entity.getTotalValue());
        stmInsert.setTimestamp(4, Timestamp.from(entity.getSaleDate()));
        stmInsert.setString(5, entity.getStatus().name());
    }

    @Override
    protected void setParamsQueryDelete(PreparedStatement stmDelete, String value) throws SQLException {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    protected void setParamsQueryUpdate(PreparedStatement stmUpdate, Sale entity) throws SQLException {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    protected void setParamsQuerySelect(PreparedStatement stmSelect, String value) throws SQLException {
        stmSelect.setString(1, value);
    }


}
