package uz.maniac4j.data.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.maniac4j.data.entity.ModbusClient;
import uz.maniac4j.data.entity.ModbusItem;
import uz.maniac4j.data.entity.MqttClient;

@Service
public class MqttClientService {

    private final MqttClientRepository repository;

    @Autowired
    public MqttClientService(MqttClientRepository repository) {
        this.repository = repository;
    }

    public Optional<MqttClient> get(UUID id) {
        return repository.findById(id);
    }

    public MqttClient update(MqttClient entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
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

}
