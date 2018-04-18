package com.vacry.vatty.factory;

/**
 * 作为IOC容器
 * @author VacryLeaf
 *
 */
public class VattyFactory
{
    private static VattyFactory factory;
    static
    {
        factory = new VattyFactory();
    }

    private VattyFactory()
    {
    }

    public static VattyFactory getInstance()
    {
        return factory;
    }
}
