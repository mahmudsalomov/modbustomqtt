package uz.maniac4j.data.service;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.maniac4j.data.entity.ModbusClient;
import uz.maniac4j.data.entity.ModbusItem;

@Service
public class ModbusItemService {

    private final ModbusItemRepository repository;

    private final ModbusClientRepository modbusClientRepository;

    @Autowired
    public ModbusItemService(ModbusItemRepository repository, ModbusClientRepository modbusClientRepository) {
        this.repository = repository;
        this.modbusClientRepository = modbusClientRepository;
    }

    public Optional<ModbusItem> get(UUID id) {
        return repository.findById(id);
    }

    public ModbusItem update(ModbusItem entity) {
        ModbusItem save = repository.save(entity);
        save.getModbusClient().getItems().add(save);
        modbusClientRepository.save(save.getModbusClient());
        return save;
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<ModbusItem> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<ModbusItem> list(Pageable pageable, ModbusClient client) {
        Page<ModbusItem> all = repository.findAllByModbusClient(pageable,client);
        System.out.println(all.getContent());
        return repository.findAllByModbusClient(pageable,client);
    }

    public int count() {
        return (int) repository.count();
    }

}
