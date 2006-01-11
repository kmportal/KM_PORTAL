package com.ibm.km.exception;

public class KmRuleMstrDaoException extends DAOException {

    public KmRuleMstrDaoException(String message) {
        super(message);
    }

    public KmRuleMstrDaoException(String message, Throwable throwable) {
        super(message,throwable);
    }

}
