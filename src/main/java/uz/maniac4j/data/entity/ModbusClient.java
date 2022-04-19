package uz.maniac4j.data.entity;

import javax.persistence.Entity;

@Entity
public class ModbusClient extends AbstractEntity {

    private String name;
    private String ip;
    private Integer port;
    private Integer polling;
    private Integer slaveId;
    private boolean enable;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public Integer getPolling() {
        return polling;
    }
    public void setPolling(Integer polling) {
        this.polling = polling;
    }
    public Integer getSlaveId() {
        return slaveId;
    }
    public void setSlaveId(Integer slaveId) {
        this.slaveId = slaveId;
    }
    public boolean isEnable() {
        return enable;
    }
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

}
