package org.kvn.DigitalLibrary.exception;

import java.util.concurrent.Executors;

public class BookException extends Exception {
    public BookException(String msg){
        super(msg);
    }
}
