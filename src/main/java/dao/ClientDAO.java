package dao;

import dao.generic.GenericDAO;
import domain.Client;
import exception.DAOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDAO extends GenericDAO <Client, Long> implements IClientDAO {

    public ClientDAO() {
        super();
    }

    @Override
    public Class<Client> getClassType() {
        return Client.class;
    }

    @Override
    public void updateData(Client entity, Client entityRegistered) {
        entityRegistered.setCode(entity.getCode());
        entityRegistered.setName(entity.getName());
        entityRegistered.setGender(entity.getGender());
        entityRegistered.setPhone(entity.getPhone());

        entityRegistered.setHomeAddress(entity.getHomeAddress());
        entityRegistered.setCity(entity.getCity());
        entityRegistered.setState(entity.getState());
    }

    @Override
    protected String getQueryInsert() {
        return "INSERT INTO TB_CLIENT " +
                "(ID, NAME, CODE, GENDER, PHONE, HOMEADRESS, CITY, STATE ) " +
                "VALUES (nextval('sq_cliente'),?,?,?,?,?,?,?,?)";
    }

    @Override
    protected String getQueryDelete() {
        return "DELETE FROM TB_CLIENT WHERE CODE = ?";
    }

    @Override
    protected String getQueryUpdate() {
        return "UPDATE TB_CLIENT SET NAME = ?, PHONE = ?, ADRESS = ?, CITY = ?, STATE = ?, WHERE CODE = ?";
    }

    @Override
    protected void setParamsQueryInsert(PreparedStatement stmInsert, Client entity) throws SQLException {
        stmInsert.setString(1, entity.getName());
        stmInsert.setLong(2, entity.getCode());
        stmInsert.setLong(3, entity.getPhone());
        stmInsert.setString(4, entity.getGender());
        stmInsert.setString(5, entity.getHomeAddress());
        stmInsert.setString(6, entity.getCity());
        stmInsert.setString(7, entity.getState());
    }

    @Override
    protected void setParamsQueryDelete(PreparedStatement stmDelete, Long value) throws SQLException {
        stmDelete.setLong(1, value);
    }

    @Override
    protected void setParamsQueryUpdate(PreparedStatement stmRefresh, Client entity) throws SQLException {
        stmRefresh.setString(1, entity.getName());
        stmRefresh.setLong(2, entity.getCode());
        stmRefresh.setLong(3, entity.getPhone());
        stmRefresh.setString(4, entity.getGender());
        stmRefresh.setString(5, entity.getHomeAddress());
        stmRefresh.setString(6, entity.getCity());
        stmRefresh.setString(7, entity.getState());
    }

    @Override
    protected void setParamsQuerySelect(PreparedStatement stmSelect, Long value) throws SQLException {
        stmSelect.setLong(1, value);
    }

}