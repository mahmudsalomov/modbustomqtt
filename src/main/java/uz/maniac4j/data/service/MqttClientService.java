package uz.maniac4j.data.service;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

    public int count() {
        return (int) repository.count();
    }

}
