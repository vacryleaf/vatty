package com.vacry.vatty.base;

import com.vacry.vatty.type.ResultData;
import com.vacry.vatty.util.JsonUtil;

public abstract class BaseController
{
    public static int pageNumber = 1;

    public static int pageSize = 10;

    /**
     * @param text 需要转换JSON数据
     * @param clazz JOSN所属类型
     * @return 泛型对象
     */
    protected <T> T parseObject(String text, Class<T> clazz)
    {
        return JsonUtil.parseObject(text, clazz);
    }

    /**
     * JSON
     * @param obj Java对象
     * @return 页面JSON的Java对象
     */
    protected String toJSONString(Object obj)
    {
        return JsonUtil.toJSONString(obj);
    }

    /**
     * 生成返回json字符串
     * @param succeed 是否成功
     * @param errorCode 错误码
     * @param errorMsg 错误信息
     * @param data 数据
     * @return JSON字符串
     */
    protected <T> String getResultJSONStr(boolean succeed, T data, String errorCode, String errorMsg)
    {
        ResultData<T> ret = getResultData(succeed, data, errorCode);
        ret.setErrorMsg(errorMsg);
        String retJSONStr = JsonUtil.toJSONString(ret);
        ret = null;
        return retJSONStr;
    }

    /**
     * 生成返回json字符串
     * @param succeed 是否成功
     * @param errorCode 错误码
     * @param data 数据
     * @return JSON字符串
     */
    protected <T> String getResultJSONStr(boolean succeed, T data, String errorCode, Object... args)
    {
        ResultData<T> ret = getResultData(succeed, data, errorCode, args);
        String retJSONStr = JsonUtil.toJSONString(ret);
        ret = null;
        return retJSONStr;
    }

    /**
     * 返回ResultData
     * @param <T> 泛型对象
     * @param succeed 是否成功
     * @param errorCode 错误码
     * @param data 数据对象
     * @param args 读取额外参数
     * @return 返回统一的数据结构
     */
    protected <T> ResultData<T> getResultData(boolean succeed, T data, String errorCode, Object... args)
    {
        ResultData<T> ret = new ResultData<T>();
        ret.setSucceed(succeed);
        ret.setErrorCode(errorCode);
        if(null != args && args.length > 1)
        {
            ret.setErrorMsg((String)args[0]);
        }
        ret.setData(data);
        return ret;
    }
}
