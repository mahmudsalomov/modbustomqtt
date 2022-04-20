package uz.maniac4j.data.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uz.maniac4j.data.enums.RegisterType;
import uz.maniac4j.data.enums.RegisterVarType;
import uz.maniac4j.modbus.exceptions.ModbusStormException;

import javax.persistence.*;
import java.io.IOException;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"modbus_client_id", "address"})})
public class ModbusItem extends AbstractEntity {

    private String tagName;

    @Enumerated(EnumType.STRING)
    private RegisterType register;

    @Enumerated(EnumType.STRING)
    private RegisterVarType type;
    private Integer address;

    @ManyToOne
    @JoinColumn(name = "modbus_client_id")
    private ModbusClient modbusClient;

    public String getTagName() {
        return tagName;
    }
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
    public String getRegister() {
        return register!=null?register.name():"";
    }
    public void setRegister(String register) {
        this.register=RegisterType.valueOf(register);
    }
    public String getType() {
        return type!=null?type.name():"";
    }
    public void setType(String type) {
        this.type=RegisterVarType.valueOf(type);
    }
    public Integer getAddress() {
        return address;
    }
    public void setAddress(Integer address) {
        this.address = address;
    }

    public ModbusClient getModbusClient() {
        return modbusClient;
    }

    public void setModbusClient(ModbusClient modbusClient) {
        this.modbusClient = modbusClient;
    }

    public boolean validCheck(){
        return tagName != null && !tagName.isEmpty() && register != null && type != null && address!=null && address>=0 && address<65536;
    }


    public String getValue() {

        if (modbusClient==null) return "error";
        if (!modbusClient.isEnable()) return "Client switch off";
        try {
            ModbusClient connect = modbusClient.Connect();
            int[] ints=new int[2];
            switch (register){
                case INPUT -> {
                    ints = connect.getClient().ReadInputRegisters(address, 2);
                }
                case HOLDING -> {
                    ints = connect.getClient().ReadHoldingRegisters(address, 2);
                }
            }

            switch (type){
                case INT16 -> {
                    return String.valueOf(ints[0]);
                }
                case INT32 -> {
                    int rebuilt32 = (ints[0] << 16) | (ints[1] & 0xFFFF);
                    System.out.println(rebuilt32);
                    return String.valueOf(rebuilt32);
                }
//                    case FLOAT16 -> {}
                case FLOAT32 -> {
                    return String.valueOf(uz.maniac4j.modbus.client.ModbusClient.ConvertRegistersToFloat(ints, uz.maniac4j.modbus.client.ModbusClient.RegisterOrder.HighLow));
                }
            }

            return "0";
        }catch (Exception e){
            return "error";
        }




    }






}
