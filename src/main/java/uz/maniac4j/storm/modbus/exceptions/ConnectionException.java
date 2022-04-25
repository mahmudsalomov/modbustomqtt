package uz.maniac4j.storm.modbus.exceptions;

public class ConnectionException extends ModbusStormException{
    public ConnectionException() {
    }

    public ConnectionException(String s) {
        super(s);
    }
}
