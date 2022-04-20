package uz.maniac4j.data.entity;

import lombok.ToString;
import uz.maniac4j.modbus.server.ModbusServer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

@ToString
@Entity
public class ModbusClient extends AbstractEntity {

//    @NotEmpty
    @Column(unique = true)
    private String name;
//    @NotEmpty
    private String ip;
//    @NotEmpty
    private Integer port;
//    @NotEmpty
    private Integer polling;
//    @NotEmpty
    private Integer slaveId;
    private boolean enable;

    @Transient
    private uz.maniac4j.modbus.client.ModbusClient client=new uz.maniac4j.modbus.client.ModbusClient();

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


    public boolean validCheck(){
        return name != null && !name.isEmpty() && ip != null && !ip.isEmpty() && port != null && port>0&&port<65535 && polling != null && polling>=100&&polling<=100000 && slaveId != null;
    }


    public uz.maniac4j.modbus.client.ModbusClient getClient() {
        return client;
    }

    public void setClient(uz.maniac4j.modbus.client.ModbusClient client) {
        this.client = client;
    }

    public ModbusClient Connect(){
        try {
            System.out.println(ModbusServer.getAllStackTraces());
            client.setPort(port);
            client.setConnectionTimeout(polling);
            client.setIpAddress(ip);
            client.Connect();
            client.isConnected();
            return this;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

}
