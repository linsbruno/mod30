package domain;

import anotation.KeyType;
import anotation.Table;
import anotation.TableColumn;
import dao.CommonClass;

@Table("TB_CLIENT")
public class Client implements CommonClass {

    @TableColumn(dbName = "id", setJavaName = "setId")
    private Long id;

    @TableColumn(dbName = "name", setJavaName = "setName")
    private String name;

    @KeyType("getCode")
    @TableColumn(dbName = "code", setJavaName = "setCode")
    private Long code;

    @TableColumn(dbName = "phone", setJavaName = "setPhone")
    private Long phone;

    @TableColumn(dbName = "homeAddress", setJavaName = "setHomeAddress")
    private String homeAddress;

    @TableColumn(dbName = "city", setJavaName = "setCity")
    private String city;

    @TableColumn(dbName = "state", setJavaName = "setState")
    private String state;

    @TableColumn(dbName = "gender", setJavaName = "setGender")
    private String gender;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}