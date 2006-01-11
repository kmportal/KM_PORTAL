package com.ibm.km.exception;

public class KmDocumentMstrDaoException extends DAOException {

    public KmDocumentMstrDaoException(String message) {
        super(message);
    }

    public KmDocumentMstrDaoException(String message, Throwable throwable) {
        super(message,throwable);
    }

}
