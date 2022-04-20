package uz.maniac4j.data.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class MqttClient extends AbstractEntity {

    private String name;
    private String modbus;
    private Integer polling;
    private String ip;
    private Integer port;
    private String topic;
    private boolean enable;


    @ManyToOne
    private ModbusClient modbusClient;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getModbus() {
        return modbus;
    }
    public void setModbus(String modbus) {
        this.modbus = modbus;
    }
    public Integer getPolling() {
        return polling;
    }
    public void setPolling(Integer polling) {
        this.polling = polling;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public Integer getPort() {
        return port;
    }
    public void setPort(Integer port) {
        this.port = port;
    }
    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }
    public boolean isEnable() {
        return enable;
    }
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public ModbusClient getModbusClient() {
        return modbusClient;
    }

    public void setModbusClient(ModbusClient modbusClient) {
        this.modbusClient = modbusClient;
    }

}
