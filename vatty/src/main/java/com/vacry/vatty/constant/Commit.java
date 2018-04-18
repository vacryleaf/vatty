package com.vacry.vatty.constant;

public enum Commit
{
    AUTOMATIC(true), MANUAL(false);

    private final boolean code;

    Commit(boolean code)
    {
        this.code = code;
    }

    public boolean value()
    {
        return code;
    }
}
