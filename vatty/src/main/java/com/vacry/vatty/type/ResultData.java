package com.vacry.vatty.type;

public class ResultData<T>
{
    private boolean succeed;

    private String errorCode;

    private String errorMsg;

    private T data;

    public boolean isSucceed()
    {
        return succeed;
    }

    public void setSucceed(boolean succeed)
    {
        this.succeed = succeed;
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

    public String getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }
}