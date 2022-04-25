package uz.maniac4j.data.entity;

import org.apache.http.conn.util.InetAddressUtils;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"modbus_client_id", "name"})})
@Entity
public class MqttClient extends AbstractEntity {


    private String name;
//    private String modbus;
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
        return modbusClient!=null?modbusClient.getName():"";
    }
    public void setModbus(String modbus) {
//        this.modbus = modbus;
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


    public boolean validCheck(){
        return name != null &&
                !name.isEmpty() &&
                ip != null &&
                !ip.isEmpty() &&
                InetAddressUtils.isIPv4Address(ip) &&
                topic != null &&
                !topic.isEmpty() &&
                port != null &&
                port>0 &&
                port<65535 &&
                polling != null &&
                polling>=100 &&
                polling<=10000;
    }



    public Map<String,String> getJson(){
        Map<String,String> map=new HashMap<>();
        Set<ModbusItem> items = modbusClient.getItems();
//        System.out.println("SET");
//        System.out.println(modbusClient);
//        System.out.println(items.size());
        for (ModbusItem item : items) {
//            System.out.println(item);
            map.put(item.getTagName(),item.getValue());
        }
        return map;
    }



}
