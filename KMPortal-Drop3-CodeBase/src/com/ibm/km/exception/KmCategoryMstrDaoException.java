package com.ibm.km.exception;

public class KmCategoryMstrDaoException extends DAOException {

    public KmCategoryMstrDaoException(String message) {
        super(message);
    }

    public KmCategoryMstrDaoException(String message, Throwable throwable) {
        super(message,throwable);
    }

}
