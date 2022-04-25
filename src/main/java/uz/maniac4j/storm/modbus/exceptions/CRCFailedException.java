package uz.maniac4j.storm.modbus.exceptions;

public class CRCFailedException extends ModbusStormException{
    public CRCFailedException() {
    }

    public CRCFailedException(String s) {
        super(s);
    }
}
