package uz.maniac4j.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.maniac4j.data.entity.ItemLog;

import java.util.UUID;

public interface ItemLogRepository extends JpaRepository<ItemLog, Long> {

}
