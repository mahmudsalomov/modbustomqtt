package uz.maniac4j.data.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.maniac4j.data.entity.ModbusClient;

@Service
public class ModbusClientService {

    private final ModbusClientRepository repository;

    @Autowired
    public ModbusClientService(ModbusClientRepository repository) {
        this.repository = repository;
    }

    public Optional<ModbusClient> get(UUID id) {
        return repository.findById(id);
    }

    public ModbusClient update(ModbusClient entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<ModbusClient> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<ModbusClient> all(){
        return repository.findAll();
    }

    public int count() {
        return (int) repository.count();
    }

}
