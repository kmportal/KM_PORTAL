package com.ibm.km.exception;

public class KmException extends DAOException {

    public KmException(String message) {
        super(message);
    }

    public KmException(String message, Throwable throwable) {
        super(message,throwable);
    }

}
