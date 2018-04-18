package com.vacry.vatty.util;

import com.vacry.vatty.type.ResultData;

public class ResultUtil
{
    public static <T> String getResultJSONStr(boolean succeed, T data)
    {
        return getResultJSONStr(succeed, data, "", "");
    }

    public static <T> String getResultJSONStr(boolean succeed, T data, String errorCode, String errorMsg)
    {
        ResultData<T> ret = new ResultData<T>();
        ret.setSucceed(succeed);
        ret.setData(data);
        ret.setErrorCode(errorCode);
        ret.setErrorMsg(errorMsg);
        String retJSONStr = JsonUtil.toJSONString(ret);
        ret = null;
        return retJSONStr;
    }
}
