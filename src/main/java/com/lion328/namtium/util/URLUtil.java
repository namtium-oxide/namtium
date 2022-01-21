// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.util;

import com.lion328.namtium.settings.LauncherConstant;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class URLUtil {

    public static URL constantURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            LauncherConstant.LOGGER.catching(e);
        }
        return null;
    }

    public static String getETag(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String etag = connection.getHeaderField("ETag");

        if (etag == null) {
            throw new IOException("ETag header not found");
        }

        return etag;
    }

    public static String httpGET(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream is = connection.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);
        byte[] buff = new byte[8192];
        int len;
        while ((len = is.read(buff)) != -1) {
            baos.write(buff, 0, len);
        }

        return new String(baos.toByteArray(), StandardCharsets.UTF_8);
    }
}
