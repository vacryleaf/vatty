package com.vacry.vatty.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.SqlSessionManager;

import com.vacry.vatty.constant.Commit;

public class SessionUtil
{
    private static SqlSessionFactory factory;
    static
    {
        String resource = "mybatis-config.xml";
        InputStream inputStream;
        try
        {
            inputStream = Resources.getResourceAsStream(resource);
            factory = new SqlSessionFactoryBuilder().build(inputStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static SqlSession getSqlSession(Commit commit)
    {
        return factory.openSession(commit.value());
    }

    public static SqlSession getSqlSession()
    {
        return SqlSessionManager.newInstance(factory);
    }

    public static <T> T getMapper(Class<T> clazz)
    {
        return getSqlSession().getMapper(clazz);
    }
}
