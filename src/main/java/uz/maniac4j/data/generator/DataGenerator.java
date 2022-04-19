//package uz.maniac4j.data.generator;
//
//import com.vaadin.exampledata.DataType;
//import com.vaadin.exampledata.ExampleDataGenerator;
//import com.vaadin.flow.spring.annotation.SpringComponent;
//import java.time.LocalDateTime;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import uz.maniac4j.data.entity.HistoryOneItem;
//import uz.maniac4j.data.entity.ModbusClient;
//import uz.maniac4j.data.entity.ModbusItem;
//import uz.maniac4j.data.entity.MqttClient;
//import uz.maniac4j.data.service.HistoryOneItemRepository;
//import uz.maniac4j.data.service.ModbusClientRepository;
//import uz.maniac4j.data.service.ModbusItemRepository;
//import uz.maniac4j.data.service.MqttClientRepository;
//
//@SpringComponent
//public class DataGenerator {
//
//    @Bean
//    public CommandLineRunner loadData(ModbusClientRepository modbusClientRepository,
//            ModbusItemRepository modbusItemRepository, MqttClientRepository mqttClientRepository,
//            HistoryOneItemRepository historyOneItemRepository) {
//        return args -> {
//            Logger logger = LoggerFactory.getLogger(getClass());
//            if (modbusClientRepository.count() != 0L) {
//                logger.info("Using existing database");
//                return;
//            }
//            int seed = 123;
//
//            logger.info("Generating demo data");
//
//            logger.info("... generating 100 Modbus Client entities...");
//            ExampleDataGenerator<ModbusClient> modbusClientRepositoryGenerator = new ExampleDataGenerator<>(
//                    ModbusClient.class, LocalDateTime.of(2022, 4, 19, 0, 0, 0));
//            modbusClientRepositoryGenerator.setData(ModbusClient::setName, DataType.WORD);
//            modbusClientRepositoryGenerator.setData(ModbusClient::setIp, DataType.WORD);
//            modbusClientRepositoryGenerator.setData(ModbusClient::setPort, DataType.NUMBER_UP_TO_100);
//            modbusClientRepositoryGenerator.setData(ModbusClient::setPolling, DataType.NUMBER_UP_TO_1000);
//            modbusClientRepositoryGenerator.setData(ModbusClient::setSlaveId, DataType.NUMBER_UP_TO_100);
//            modbusClientRepositoryGenerator.setData(ModbusClient::setEnable, DataType.BOOLEAN_50_50);
//            modbusClientRepository.saveAll(modbusClientRepositoryGenerator.create(100, seed));
//
//            logger.info("... generating 100 Modbus Item entities...");
//            ExampleDataGenerator<ModbusItem> modbusItemRepositoryGenerator = new ExampleDataGenerator<>(
//                    ModbusItem.class, LocalDateTime.of(2022, 4, 19, 0, 0, 0));
//            modbusItemRepositoryGenerator.setData(ModbusItem::setTagname, DataType.WORD);
//            modbusItemRepositoryGenerator.setData(ModbusItem::setRegister, DataType.WORD);
//            modbusItemRepositoryGenerator.setData(ModbusItem::setType, DataType.WORD);
//            modbusItemRepositoryGenerator.setData(ModbusItem::setAddress, DataType.NUMBER_UP_TO_100);
//            modbusItemRepository.saveAll(modbusItemRepositoryGenerator.create(100, seed));
//
//            logger.info("... generating 100 Mqtt Client entities...");
//            ExampleDataGenerator<MqttClient> mqttClientRepositoryGenerator = new ExampleDataGenerator<>(
//                    MqttClient.class, LocalDateTime.of(2022, 4, 19, 0, 0, 0));
//            mqttClientRepositoryGenerator.setData(MqttClient::setName, DataType.WORD);
//            mqttClientRepositoryGenerator.setData(MqttClient::setModbus, DataType.WORD);
//            mqttClientRepositoryGenerator.setData(MqttClient::setPolling, DataType.NUMBER_UP_TO_100);
//            mqttClientRepositoryGenerator.setData(MqttClient::setIp, DataType.WORD);
//            mqttClientRepositoryGenerator.setData(MqttClient::setPort, DataType.NUMBER_UP_TO_100);
//            mqttClientRepositoryGenerator.setData(MqttClient::setTopic, DataType.WORD);
//            mqttClientRepositoryGenerator.setData(MqttClient::setEnable, DataType.BOOLEAN_50_50);
//            mqttClientRepository.saveAll(mqttClientRepositoryGenerator.create(100, seed));
//
//            logger.info("... generating 100 History One Item entities...");
//            ExampleDataGenerator<HistoryOneItem> historyOneItemRepositoryGenerator = new ExampleDataGenerator<>(
//                    HistoryOneItem.class, LocalDateTime.of(2022, 4, 19, 0, 0, 0));
//            historyOneItemRepositoryGenerator.setData(HistoryOneItem::setDate, DataType.DATETIME_LAST_10_YEARS);
//            historyOneItemRepositoryGenerator.setData(HistoryOneItem::setValue, DataType.NUMBER_UP_TO_100);
//            historyOneItemRepositoryGenerator.setData(HistoryOneItem::setStatus, DataType.WORD);
//            historyOneItemRepository.saveAll(historyOneItemRepositoryGenerator.create(100, seed));
//
//            logger.info("Generated demo data");
//        };
//    }
//
//}