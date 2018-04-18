package com.vacry;

import com.vacry.vatty.HttpServer;

/**
 * 
 * @author VacryLeaf
 *
 */
public class Application
{
    public static void main(String[] args) throws Exception
    {
        new HttpServer(Application.class).start(10001);
    }
}
