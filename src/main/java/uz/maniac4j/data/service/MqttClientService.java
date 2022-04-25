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
                    client.connect();
//                    new MqttMessage();

//                    new JSONPObject(mqttClient.getJson());

                    client.publish(mqttClient.getTopic(),new MqttMessage(new ObjectMapper().writeValueAsBytes(mqttClient.getJson())));
//                    client.publish(mqttClient.getTopic(),new MqttMessage(convertWithStream(getJson(mqttClient)).getBytes()));
                    System.out.println("publish");
                } catch (MqttException | JsonProcessingException
//                         | JsonProcessingException
//                        | JsonProcessingException
                        e
                ) {
//                    e.printStackTrace();
                }
            }

        }
    }

    public static String convertWithStream(Map<String, String> map) {
        return map.keySet().stream()
                .map(key -> key + ":" + map.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }


    public Map<String,String> getJson(MqttClient client){
        Map<String,String> map=new HashMap<>();
        Set<ModbusItem> items = client.getModbusClient().getItems();
//        System.out.println("SET");
//        System.out.println(client.getModbusClient());
//        System.out.println(items.size());
        for (ModbusItem item : items) {
//            System.out.println(item);
            String value = item.getValue();
            itemLogRepository.save(ItemLog.builder().value(ErrorResponse.check(value)).modbusItem(item).build());
            map.put(item.getTagName(),value);
        }
        return map;
    }

//    public static void main(String[] args) {
//        System.out.println(ErrorResponse.valueOf("AA"));
//    }
}
