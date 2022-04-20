package uz.maniac4j.data.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.maniac4j.data.entity.ModbusClient;
import uz.maniac4j.data.entity.ModbusItem;
import uz.maniac4j.data.entity.MqttClient;

public interface MqttClientRepository extends JpaRepository<MqttClient, UUID> {
    Page<MqttClient> findAllByModbusClient(Pageable pg, ModbusClient client);
}