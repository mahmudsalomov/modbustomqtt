package uz.maniac4j.data.service;

import java.util.Optional;
import java.lang.Long;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.maniac4j.data.entity.HistoryOneItem;

@Service
public class HistoryOneItemService {

    private final HistoryOneItemRepository repository;

    @Autowired
    public HistoryOneItemService(HistoryOneItemRepository repository) {
        this.repository = repository;
    }

    public Optional<HistoryOneItem> get(Long id) {
        return repository.findById(id);
    }

    public HistoryOneItem update(HistoryOneItem entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<HistoryOneItem> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
