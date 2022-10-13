package uz.maniac4j.data.service;

import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.maniac4j.data.entity.ItemLog;
import uz.maniac4j.data.entity.ModbusClient;
import uz.maniac4j.data.entity.ModbusItem;
import uz.maniac4j.data.entity.MqttClient;
import uz.maniac4j.data.enums.ErrorResponse;

@Service
public class MqttClientService {

    private final MqttClientRepository repository;
    private final ItemLogRepository itemLogRepository;

    private boolean failed=false;

    @Autowired
    public MqttClientService(MqttClientRepository repository, ItemLogRepository itemLogRepository) {
        this.repository = repository;
        this.itemLogRepository = itemLogRepository;
    }

    public Optional<MqttClient> get(Long id) {
        return repository.findById(id);
    }

    public MqttClient update(MqttClient entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<MqttClient> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<MqttClient> list(Pageable pageable, ModbusClient client) {
        return repository.findAllByModbusClient(pageable,client);
    }

    public int count() {
        return (int) repository.count();
    }

    public List<MqttClient> list(){
        return repository.findAll();
    }


    public void publish() {
        List<MqttClient> list = list();
        for (MqttClient mqttClient : list) {
            if (mqttClient.getModbusClient().isEnable()&&mqttClient.isEnable()){
                try {
                    var client=new org.eclipse.paho.client.mqttv3.MqttClient("tcp://"+mqttClient.getIp()+":"+mqttClient.getPort(),mqttClient.getName());
                    client.setTimeToWait(1000);
//                    if (failed) client.reconnect();
//                    else
                        client.connect();
//                    new MqttMessage();

//                    new JSONPObject(mqttClient.getJson());

//                    client.publish(mqttClient.getTopic(),new MqttMessage(new ObjectMapper().writeValueAsBytes(mqttClient.getJson())));
                    client.publish(mqttClient.getTopic(),new MqttMessage(getJson(mqttClient)));
                    System.out.println("publish");
//                    failed=false;
                } catch (MqttException | JsonProcessingException
//                         | JsonProcessingException
//                        | JsonProcessingException
                        e
                ) {
                    e.printStackTrace();
                    System.out.println("publish failed");
//                    failed=true;
                }
            }

        }
    }

    public static String convertWithStream(Map<String, String> map) {
        return map.keySet().stream()
                .map(key -> key + ":" + map.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }


    public byte[] getJson(MqttClient client) throws JsonProcessingException {
        Map<String,String> map=new HashMap<>();
        Set<ModbusItem> items = client.getModbusClient().getItems();
//        System.out.println("SET");
//        System.out.println(client.getModbusClient());
//        System.out.println(items.size());
        for (ModbusItem item : items) {
//            System.out.println(item);
            String value = item.getValue();
            Double v = ErrorResponse.check(value);
            if (v!=null) itemLogRepository.save(ItemLog.builder().value(v).modbusItem(item).build());
            map.put(item.getTagName(),value);
        }
        return new ObjectMapper().writeValueAsBytes(map);
    }

//    public static void main(String[] args) {
//        System.out.println(ErrorResponse.valueOf("AA"));
//    }
}
