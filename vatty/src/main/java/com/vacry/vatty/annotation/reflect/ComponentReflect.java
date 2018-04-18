package com.vacry.vatty.annotation.reflect;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.collect.Maps;
import com.vacry.vatty.annotation.Action;
import com.vacry.vatty.annotation.Service;
import com.vacry.vatty.exception.EmptyException;
import com.vacry.vatty.exception.RepeatException;
import com.vacry.vatty.util.ClassUtil;
import com.vacry.vatty.util.StringUtil;

public class ComponentReflect
{
    private final static Logger log = Logger.getLogger(ComponentReflect.class);

    private static Map<Class<?>, Object> serviceMap;

    private static Map<String, Class<?>> actionMap;

    static
    {
        serviceMap = Maps.newHashMap();
        actionMap = Maps.newHashMap();
    }

    public static Object getService(Class<?> clazz)
    {
        return serviceMap.get(clazz);
    }

    public static Class<?> getAction(String url)
    {
        return actionMap.get(url);
    }

    public static void init(Class<?> clazz) throws EmptyException, RepeatException
    {
        Package pac = clazz.getPackage();
        List<Class<?>> list = ClassUtil.getAllClass(pac.getName());
        log.info("init service");
        for(Class<?> c : list)
        {
            Annotation[] annotations = c.getAnnotations();
            for(Annotation annotation : annotations)
            {
                if(annotation instanceof Service)
                {
                    try
                    {
                        Object o2 = c.newInstance();
                        serviceMap.put(c, o2);
                    }
                    catch (InstantiationException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if(annotation instanceof Action)
                {
                    Action rm = (Action)annotation;
                    String url = rm.value();
                    if(StringUtil.isEmpty(url))
                    {
                        throw new EmptyException("Action的value不能为空");
                    }
                    Class<?> cla = actionMap.get(url);
                    if(null != cla)
                    {
                        throw new RepeatException("Action的value不能重复");
                    }
                    actionMap.put(rm.value(), c);
                }
            }
        }
    }
}
