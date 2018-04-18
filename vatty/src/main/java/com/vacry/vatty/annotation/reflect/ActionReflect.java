package com.vacry.vatty.annotation.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.vacry.vatty.annotation.RequestMapping;

public class ActionReflect
{
    private final Logger log = Logger.getLogger(getClass());

    private ActionReflect()
    {
    }

    private static ActionReflect reflect;
    static
    {
        reflect = new ActionReflect();
    }

    public static ActionReflect getInstance()
    {
        return reflect;
    }

    public String requestDispatch(String uri, String msg)
    {
        String[] uris = uri.split("/");
        if(uris.length < 2)
        {
            log.error(String.format("uri: %s 没有'/'", uri));
            return null;
        }
        String firstUrl = "/" + uris[1];
        Class<?> clazz = ComponentReflect.getAction(firstUrl);
        if(null == clazz)
        {
            log.error(String.format("uri: %s 找不到该服务", uri));
            return null;
        }
        try
        {
            Object t = clazz.newInstance();
            String serviceName = uri.substring(firstUrl.length());
            Method[] methods = clazz.getMethods();
            for(Method method : methods)
            {
                Annotation[] annotations = method.getAnnotations();
                for(Annotation annotation : annotations)
                {
                    if(annotation instanceof RequestMapping)
                    {
                        RequestMapping rm = (RequestMapping)annotation;
                        if(serviceName.equals(rm.value()))
                        {
                            AutowiredReflect.inject(t);
                            return (String)method.invoke(t, new Object[]
                            {msg});
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            log.error("创建出错", e);
            return null;
        }
        return null;
    }
}
