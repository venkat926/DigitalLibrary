package org.kvn.DigitalLibrary.exception;

public class TxnException extends RuntimeException{
    public TxnException(String msg) {
        super(msg);
    }
}
