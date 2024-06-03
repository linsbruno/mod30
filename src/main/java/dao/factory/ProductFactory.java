package dao.factory;

import domain.Client;
import domain.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductFactory {

    public static Product convert(ResultSet rs) throws SQLException {
        Product prod = new Product();

        prod.setId(rs.getLong("ID_CLIENT"));
        prod.setName(rs.getString("NAME"));
        prod.setCode(rs.getString("CODE"));
        prod.setDescription(rs.getString("DESCRIPTION"));
        prod.setValue(rs.getBigDecimal("VALUE"));
        prod.setUnit(rs.getString("UNITS"));
        return prod;
    }
}