package domain;

import anotation.KeyType;
import anotation.Table;
import anotation.TableColumn;

import java.math.BigDecimal;

@Table("TB_PRODUCT_QTD")
public class ProductQtd {

    @TableColumn(dbName = "id", setJavaName = "setId")
    private Long id;

    private Product product;
    @TableColumn(dbName = "qtd", setJavaName = "setQtd")
    private Integer qtd;

    @TableColumn(dbName = "totalValue", setJavaName = "setTotalValue")
    private BigDecimal totalValue;

    public ProductQtd() {
        this.qtd = 0;
        this.totalValue = BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }


    public void toAdd(Integer qtd) {
        this.qtd += qtd;
        BigDecimal newValue = this.product.getValue().multiply(BigDecimal.valueOf(qtd));
        BigDecimal newTotal = this.totalValue.add(newValue);
        this.totalValue = newTotal;
    }

    public void toRemove(Integer qtd) {
        this.qtd += qtd;
        BigDecimal newValue = this.product.getValue().multiply(BigDecimal.valueOf(qtd));
        this.totalValue = this.totalValue.subtract(newValue);
    }
}