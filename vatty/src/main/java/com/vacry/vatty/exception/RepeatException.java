package com.vacry.vatty.exception;

public class RepeatException extends Exception
{
    private static final long serialVersionUID = 939296775194410066L;

    public RepeatException()
    {
        super();
    }

    public RepeatException(String message)
    {
        super(message);
    }

    public RepeatException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public RepeatException(Throwable cause)
    {
        super(cause);
    }

}