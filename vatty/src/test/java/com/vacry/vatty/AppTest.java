package com.vacry.vatty;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    

    public static void test()
    {
        String url = "src/main/resource";
        File file = new File(url);
        if(!file.isDirectory())
        {
            System.out.println("文件");
            System.out.println("path=" + file.getPath());
            System.out.println("absolutepath=" + file.getAbsolutePath());
            System.out.println("name=" + file.getName());
        }
        else if(file.isDirectory())
        {
            System.out.println("文件夹");
            String[] filelist = file.list();
            for(int i = 0; i < filelist.length; i++)
            {
                File readfile = new File(url + "\\" + filelist[i]);
                if(!readfile.isDirectory())
                {
                    System.out.println("path=" + readfile.getPath());
                    System.out.println("absolutepath=" + readfile.getAbsolutePath());
                    System.out.println("name=" + readfile.getName());

                }
            }
        }
    }
}
