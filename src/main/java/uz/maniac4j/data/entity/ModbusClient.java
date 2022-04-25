package uz.maniac4j.data.entity;

import lombok.ToString;
import org.apache.http.conn.util.InetAddressUtils;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

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


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<ModbusItem> items=new HashSet<>();

    @Transient
    private uz.maniac4j.storm.modbus.client.ModbusClient client=new uz.maniac4j.storm.modbus.client.ModbusClient();

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

    public Set<ModbusItem> getItems() {
        return items;
    }

    public void setItems(Set<ModbusItem> items) {
        this.items = items;
    }

    public boolean validCheck(){
        return name != null &&
                !name.isEmpty() &&
                ip != null &&
                !ip.isEmpty() &&
                InetAddressUtils.isIPv4Address(ip) &&
                port != null &&
                port>0 &&
                port<65535 &&
                polling != null &&
                polling>=100 &&
                polling<=100000 &&
                slaveId != null;
    }


    public uz.maniac4j.storm.modbus.client.ModbusClient getClient() {
        return client;
    }

    public void setClient(uz.maniac4j.storm.modbus.client.ModbusClient client) {
        this.client = client;
    }

    public ModbusClient Connect(){
        try {
//            System.out.println(ModbusServer.getAllStackTraces());
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
