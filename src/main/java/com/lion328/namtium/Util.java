package com.lion328.namtium;

import java.net.MalformedURLException;
import java.net.URL;

public class Util
{

    public static URL staticURL(String s)
    {
        try
        {
            return new URL(s);
        }
        catch (MalformedURLException e)
        {
            Main.getLogger().catching(e);
        }

        return null;
    }
}
