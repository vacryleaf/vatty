package com.vacry.vatty.exception;

public class EmptyException extends Exception
{
    private static final long serialVersionUID = -4649472013640604569L;

    public EmptyException()
    {
        super();
    }

    public EmptyException(String message)
    {
        super(message);
    }

    public EmptyException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public EmptyException(Throwable cause)
    {
        super(cause);
    }

}
