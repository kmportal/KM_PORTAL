package com.ibm.km.exception;

public class KmActorsDaoException extends DAOException {

    public KmActorsDaoException(String message) {
        super(message);
    }

    public KmActorsDaoException(String message, Throwable throwable) {
        super(message,throwable);
    }

}
