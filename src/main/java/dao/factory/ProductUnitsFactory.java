package dao.factory;

import domain.Product;
import domain.ProductQtd;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductUnitsFactory {

    public static ProductQtd convert(ResultSet rs) throws SQLException {
        Product prod = ProductFactory.convert(rs);
        ProductQtd prodQ = new ProductQtd();

        prodQ.setId(rs.getLong("ID"));
        prodQ.setProduct(prod);
        prodQ.setQtd(rs.getInt("UNITS"));
        prodQ.setTotalValue(rs.getBigDecimal("VALUE_TOTAL"));

        return prodQ;
    }
}