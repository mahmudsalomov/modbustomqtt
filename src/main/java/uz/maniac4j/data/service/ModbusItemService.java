package uz.maniac4j.data.service;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.maniac4j.data.entity.ModbusItem;

@Service
public class ModbusItemService {

    private final ModbusItemRepository repository;

    @Autowired
    public ModbusItemService(ModbusItemRepository repository) {
        this.repository = repository;
    }

    public Optional<ModbusItem> get(UUID id) {
        return repository.findById(id);
    }

    public ModbusItem update(ModbusItem entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<ModbusItem> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
