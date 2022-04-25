package uz.maniac4j.storm.modbus.exceptions;

public class InvalidStartAddressException extends ModbusStormException{
    public InvalidStartAddressException() {
    }

    public InvalidStartAddressException(String s) {
        super(s);
    }
}
