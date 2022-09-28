package uz.maniac4j.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uz.maniac4j.data.entity.MqttClient;
import uz.maniac4j.data.service.MqttClientService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Service
public class ScheduledTasks {
    @Autowired
    private MqttClientService service;
    @Scheduled(fixedDelay = 200)
    public void transform() throws InterruptedException {
        service.publish();
    }


    public static String convertWithStream(Map<String, String> map) {
        return map.keySet().stream()
                .map(key -> key + "=" + map.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
