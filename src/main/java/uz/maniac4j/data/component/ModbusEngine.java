package uz.maniac4j.data.component;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.stereotype.Component;
import uz.maniac4j.data.entity.ModbusClient;
import uz.maniac4j.data.entity.ModbusItem;
import uz.maniac4j.data.service.ModbusClientService;
import uz.maniac4j.data.service.ModbusItemService;
import uz.maniac4j.data.service.MqttClientService;

import java.util.ArrayList;
import java.util.List;

@Component
public class ModbusEngine {
    private final ModbusClientService modbusClientService;
    private final ModbusItemService modbusItemService;
    private final MqttClientService mqttClientService;

    private final List<ModbusClient> clientList=new ArrayList<>();

    public ModbusEngine(ModbusClientService modbusClientService, ModbusItemService modbusItemService, MqttClientService mqttClientService) {
        this.modbusClientService = modbusClientService;
        this.modbusItemService = modbusItemService;
        this.mqttClientService = mqttClientService;
    }





    public void t(){
//        MqttClient mqttClient=new MqttClient();
//        mqttClient.publish();
    }

}
