package dao.factory;

import domain.Client;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientFactory {

    public static Client convert(ResultSet rs) throws SQLException {
        Client client = new Client();

        client.setId(rs.getLong("ID_CLIENT"));
        client.setName(rs.getString("NAME"));
        client.setCode(rs.getLong("CODE"));
        client.setGender(rs.getString("GENDER"));
        client.setPhone(rs.getLong("PHONE"));

        client.setHomeAddress(rs.getString("HOMEADRESS"));
        client.setCity(rs.getString("CITY"));
        client.setState(rs.getString("STATE"));
        return client;
    }
}