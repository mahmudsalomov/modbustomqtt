package uz.maniac4j.data.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ModbusItem extends AbstractEntity {

    private String tagName;
    private String register;
    private String type;
    private Integer address;

    @ManyToOne
    private ModbusClient modbusClient;

    public String getTagName() {
        return tagName;
    }
    public void setTagName(String tagName) {
        this.tagName = tagName;
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

    public ModbusClient getModbusClient() {
        return modbusClient;
    }

    public void setModbusClient(ModbusClient modbusClient) {
        this.modbusClient = modbusClient;
    }
}
