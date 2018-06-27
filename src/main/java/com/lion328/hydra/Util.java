package com.lion328.hydra;

import com.lion328.xenonlauncher.util.OS;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Util
{

    private static char[] unitTable = new char[] {'\0', 'K', 'M', 'G', 'T'};
    private static File workingJar;

    public static String httpGET(URL url) throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream is = connection.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);
        byte[] buff = new byte[8192];
        int len;
        while ((len = is.read(buff)) != -1)
        {
            baos.write(buff, 0, len);
        }

        return new String(baos.toByteArray(), StandardCharsets.UTF_8);
    }

    public static List<File> listFiles(File directory, boolean withDirectory)
    {
        List<File> fileList = new ArrayList<>();

        if (!directory.exists())
        {
            return null;
        }

        File[] files = directory.listFiles();

        if (files == null)
        {
            return null;
        }

        List<File> tmp;

        for (File file : files)
        {
            if (file.isDirectory())
            {
                tmp = listFiles(file, withDirectory);

                if (tmp != null)
                {
                    fileList.addAll(tmp);
                }

                if (withDirectory)
                {
                    fileList.add(file);
                }
            }
            else
            {
                fileList.add(file);
            }
        }

        return fileList;
    }

    public static String convertUnit(long l)
    {
        int unit = 0;
        float f = l;

        while (f >= 1024 && (unit + 1 <= unitTable.length))
        {
            f *= 0.0009765625F; // 1 / 1024
            unit++;
        }

        if (unit == 0)
        {
            return String.valueOf(l);
        }

        return String.format("%.2f", f) + " " + unitTable[unit] + "i";
    }

    public static File getWorkingJar()
    {
        if (workingJar == null)
        {
            try
            {
                workingJar = new File(Util.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            }
            catch (URISyntaxException e)
            {
                HydraLauncher.getLogger().catching(e);
            }
        }

        return workingJar;
    }

    public static File getGameDirectory(String serverName)
    {
        return new File(OS.getApplicationDataDirectory(), HydraLauncher.LAUNCHER_DIRECTORY_NAME + File.separator + serverName);
    }

    public static void openURL(URL url)
    {
        if (Desktop.isDesktopSupported())
        {
            try
            {
                Desktop.getDesktop().browse(url.toURI());
            }
            catch (IOException | URISyntaxException e)
            {
                HydraLauncher.getLogger().catching(e);
            }
        }
    }
}
