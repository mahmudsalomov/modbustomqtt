package uz.maniac4j.data.service;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.maniac4j.data.entity.HistoryOneItem;

public interface HistoryOneItemRepository extends JpaRepository<HistoryOneItem, Long> {

}