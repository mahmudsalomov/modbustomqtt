package uz.maniac4j.data.entity;

import javax.persistence.Entity;

@Entity
public class ModbusItem extends AbstractEntity {

    private String tagname;
    private String register;
    private String type;
    private Integer address;

    public String getTagname() {
        return tagname;
    }
    public void setTagname(String tagname) {
        this.tagname = tagname;
    }
    public String getRegister() {
        return register;
    }
    public void setRegister(String register) {
        this.register = register;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Integer getAddress() {
        return address;
    }
    public void setAddress(Integer address) {
        this.address = address;
    }

}
