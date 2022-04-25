
package uz.maniac4j.storm.io;

public final class NoSuchPortException extends Exception {

    NoSuchPortException(String str) {
        super(str);
    }

    public NoSuchPortException() {
        super();
    }
}
