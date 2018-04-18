package com.vacry.vatty.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

/*
 * 针对json 进行解析的工具类
 * @author Alan
 *
 */
public abstract class JsonUtil
{

    public static <T> T parseObject(String text, TypeReference<T> type, Feature... features)
    {
        return JSONObject.parseObject(text, type, features);
    }

    public static String toJSONString(Object object)
    {
        return JSONObject.toJSONString(object, SerializerFeature.WriteEnumUsingToString,
                                       SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero,
                                       SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty,
                                       SerializerFeature.WriteNullBooleanAsFalse);
    }

    public static String toJSONString(Object object, SerializerFeature... features)
    {
        return JSONObject.toJSONString(object, features);
    }

    public static <T> T parseObject(String text, Class<T> clazz)
    {
        return JSONObject.parseObject(text, clazz);
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz)
    {
        return JSONObject.parseArray(text, clazz);
    }

    public static Object getValueBykey(String text, Object key)
    {
        JSONObject jsonObject = JSON.parseObject(text);
        return jsonObject.get(key);
    }

    public static JSONObject parseObject(String text)
    {
        return JSONObject.parseObject(text);
    }
}