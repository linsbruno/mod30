package domain;

import anotation.KeyType;
import anotation.Table;
import anotation.TableColumn;
import dao.CommonClass;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Table("TB_SALE")
public class Sale implements CommonClass {

    public enum Status {
        INICIADA, CONCLUIDA, CANCELADA;

        public static Status getByName(String value) {
            for (Status status : Status.values()) {
                if (status.name().equals(value)) {
                    return status;
                }
            }
            return  null;
        }
    }


    @TableColumn(dbName = "id", setJavaName = "setId")
    private Long id;

    @KeyType("getProductCode")
    @TableColumn(dbName = "code", setJavaName = "setCode")
    private String code;

    @TableColumn(dbName = "idClient", setJavaName = "setIdCLient")
    private Client client;

    private Set<ProductQtd> products;

    @TableColumn(dbName = "totalValue", setJavaName = "setTotalValue")
    private BigDecimal totalValue;

    @TableColumn(dbName = "saleDate", setJavaName = "setSaleDate")
    private Instant saleDate;

    @TableColumn(dbName = "saleStatus", setJavaName = "setSaleStatus")
    private Status status;

    public Sale() {
        products = new HashSet<>();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<ProductQtd> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductQtd> products) {
        this.products = products;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public Instant getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Instant saleDate) {
        this.saleDate = saleDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void toAddProduct(Product product, Integer qtd) {
        validateStatus();
        Optional<ProductQtd> op = products
                .stream()
                .filter(filter -> filter.getProduct().getCode().equals(product.getCode()))
                .findAny();
        if(op.isPresent()) {
            ProductQtd productQtd = op.get();
            productQtd.toAdd(qtd);
        } else {
            ProductQtd prod = new ProductQtd();
            prod.setProduct(product);
            prod.toAdd(qtd);
            products.add(prod);
        }
        RecalculateTotalValueSale();
    }

    private void validateStatus() {
        if (this.status == Status.CONCLUIDA) {
            throw new UnsupportedOperationException("IMPOSSÍVEL ALTERAR VENDA FINALIZADA");
        }
    }

    public void toRemoveProduct(Product product, Integer qtd) {
        validateStatus();
        Optional<ProductQtd> op = products
                .stream()
                .filter(filter -> filter.getProduct().getCode().equals(product.getCode()))
                .findAny();
        if (op.isPresent()) {
            ProductQtd productQtd = op.get();
            if (productQtd.getQtd()>qtd) {
                productQtd.toRemove(qtd);
                RecalculateTotalValueSale();
            } else {
                products.remove(op.get());
                RecalculateTotalValueSale();
            }
        }
    }


    public void toRemoveAll() {
        validateStatus();
        products.clear();
        totalValue = BigDecimal.ZERO;
    }

    public Integer getTotalProductsQtd() {
        // Soma a quantidade getQtd() de todos os objetos ProductQtd
        int result = products
                .stream()
                .reduce(0,
                        (partialCountResult, prod) -> partialCountResult + prod.getQtd(),
                        Integer::sum);
        return result;
    }

    private void RecalculateTotalValueSale() {

        BigDecimal totalValue = BigDecimal.ZERO;
        for (ProductQtd prod : this.products) {
            totalValue = totalValue.add(prod.getTotalValue());
        }
        this.totalValue = totalValue;
    }

}