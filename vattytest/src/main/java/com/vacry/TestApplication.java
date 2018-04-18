package com.vacry;

import com.vacry.vattytest.TestClient;

/**
 * 
 * @author VacryLeaf
 *
 */
public class TestApplication
{
    public static void main(String[] args) throws Exception
    {
        TestClient test = new TestClient("192.168.11.3", 11111);
        test.put("哈哈哈");
        test.start();
    }
}
