package com.vacry.vatty.annotation.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.vacry.vatty.annotation.Autowired;
import com.vacry.vatty.util.ClassUtil;
import com.vacry.vatty.util.SessionUtil;

public class AutowiredReflect
{
    public static boolean inject(Object o)
    {
        // 获取Class
        Class<? extends Object> c = o.getClass();
        /* 获得域 */
        Field[] fields = c.getDeclaredFields();
        // 返回值用于判断是否完成这个方法
        boolean b = false;
        // 遍历域
        for(Field field : fields)
        {
            // 获取域中的注解，遍历注解
            Annotation[] anns = field.getAnnotations();
            for(Annotation ann : anns)
            {
                if(ann instanceof Autowired)
                {
                    Class<?> c2 = (Class<?>)field.getGenericType();
                    Annotation[] autoClassAnnotations = c2.getAnnotations();
                    // 没有类注解
                    if(autoClassAnnotations.length < 1)
                    {
                        try
                        {
                            // 默认是Service，返回一个初始化时已有的实体。
                            List<Class<?>> list = ClassUtil.getAllClassByInterface(c2);
                            if(!list.isEmpty())
                            {
                                Object o2 = ComponentReflect.getService(list.get(0));
                                if(null != o2)
                                {
                                    AutowiredReflect.inject(o2);
                                    field.setAccessible(true);
                                    field.set(o, o2);
                                    b = true;
                                }
                            }
                            return b;
                        }
                        catch (IllegalAccessException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    // 如果有类注解
                    else
                    {
                        for(Annotation autoClassAnnotation : autoClassAnnotations)
                        {
                            // 如果这个类的注解是Mapper
                            if(autoClassAnnotation instanceof Mapper)
                            {
                                try
                                {
                                    // 注入一个全新的Mapper
                                    Object o2 = SessionUtil.getMapper(c2);
                                    field.setAccessible(true);
                                    field.set(o, o2);
                                    b = true;
                                }
                                catch (IllegalArgumentException e)
                                {
                                    e.printStackTrace();
                                }
                                catch (IllegalAccessException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
        return b;
    }
}
