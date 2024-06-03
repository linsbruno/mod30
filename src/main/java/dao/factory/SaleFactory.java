package dao.factory;

import domain.Client;
import domain.Product;
import domain.Sale;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SaleFactory {

    public static Sale convert(ResultSet rs) throws SQLException {
        Client client = ClientFactory.convert(rs);
        Sale sale = new Sale();

        sale.setClient(client);
        sale.setId(rs.getLong("ID_SALE"));
        sale.setCode(rs.getString("CODE"));
        sale.setTotalValue(rs.getBigDecimal("VALUE_TOTAL"));
        sale.setSaleDate(rs.getTimestamp("DATE_SALE").toInstant());
        sale.setStatus(Sale.Status.getByName(rs.getString("STATUS_SALE")));
        return sale;
    }
}