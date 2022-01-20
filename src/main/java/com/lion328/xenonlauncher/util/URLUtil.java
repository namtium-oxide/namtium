// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.util;

import com.lion328.xenonlauncher.settings.LauncherConstant;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URLUtil
{

    public static URL constantURL(String url)
    {
        try
        {
            return new URL(url);
        }
        catch (MalformedURLException e)
        {
            LauncherConstant.LOGGER.catching(e);
        }
        return null;
    }

    public static String getETag(URL url) throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String etag = connection.getHeaderField("ETag");

        if (etag == null)
        {
            throw new IOException("ETag header not found");
        }

        return etag;
    }
}
