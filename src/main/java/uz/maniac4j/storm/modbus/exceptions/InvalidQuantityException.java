package uz.maniac4j.storm.modbus.exceptions;

public class InvalidQuantityException extends ModbusStormException{
    public InvalidQuantityException() {
    }

    public InvalidQuantityException(String s) {
        super(s);
    }
}
