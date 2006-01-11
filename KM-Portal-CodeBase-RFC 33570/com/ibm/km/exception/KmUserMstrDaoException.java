package com.ibm.km.exception;

public class KmUserMstrDaoException extends DAOException {

    public KmUserMstrDaoException(String message) {
        super(message);
    }

    public KmUserMstrDaoException(String message, Throwable throwable) {
        super(message,throwable);
    }

}
