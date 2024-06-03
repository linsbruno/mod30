package domain;

import anotation.KeyType;
import anotation.Table;
import anotation.TableColumn;
import dao.CommonClass;

import java.math.BigDecimal;

@Table("TB_PRODUCT")
public class Product implements CommonClass {

    @TableColumn(dbName = "id", setJavaName = "setId")
    private Long id;

    @KeyType("getProductCode")
    @TableColumn(dbName = "code", setJavaName = "setCode")
    private String code;

    @TableColumn(dbName = "name", setJavaName = "setName")
    private String name;

    @TableColumn(dbName = "description", setJavaName = "setDescription")
    private String description;

    @TableColumn(dbName = "value", setJavaName = "setValue")
    private BigDecimal value;

    @TableColumn(dbName = "units", setJavaName = "setUnits")
    private String unit;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
