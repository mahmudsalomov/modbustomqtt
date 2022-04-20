package uz.maniac4j.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uz.maniac4j.data.entity.MqttClient;
import uz.maniac4j.data.service.MqttClientService;

import java.util.List;

@Component
@Service
public class ScheduledTasks {
    @Autowired
    private MqttClientService service;
    @Scheduled(fixedRate = 400)
    public void transform() throws InterruptedException {
        List<MqttClient> list = service.list();
        for (MqttClient mqttClient : list) {
            if (mqttClient.getModbusClient().isEnable()&&mqttClient.isEnable()){
                try {
                    org.eclipse.paho.client.mqttv3.MqttClient client=new org.eclipse.paho.client.mqttv3.MqttClient("tcp://"+mqttClient.getIp()+":"+mqttClient.getPort(),mqttClient.getName());
                    client.connect();
                    new MqttMessage();
                    client.publish(mqttClient.getTopic(),new MqttMessage(new ObjectMapper().writeValueAsBytes(mqttClient.getJson())));

                } catch (MqttException | JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
