/*
 * Copyright (c) 2018 Waritnan Sookbuntherng
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.lion328.xenonlauncher.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class URLFileDownloader implements FileDownloader
{

    public static final int BUFFER_SIZE = 8192;

    private final URL inputUrl;
    private final File targetFile;
    private final int bufferSize;
    private final List<DownloaderCallback> callbackList;

    private boolean running;
    private int percentage;
    private long size;
    private long downloaded;

    public URLFileDownloader(URL url, File file)
    {
        this(url, file, BUFFER_SIZE);
    }

    public URLFileDownloader(URL url, File file, int bufferSize)
    {
        inputUrl = url;
        targetFile = file;
        this.bufferSize = bufferSize;
        callbackList = new ArrayList<>();
        running = false;
    }

    @Override
    public synchronized void download() throws IOException
    {
        if (running)
        {
            return;
        }

        running = true;

        HttpURLConnection connection = (HttpURLConnection) inputUrl.openConnection();
        connection.setRequestMethod("GET");

        size = connection.getContentLengthLong();
        downloaded = 0;
        percentage = 0;

        if (!targetFile.getParentFile().exists() && !targetFile.getParentFile().mkdirs())
        {
            throw new IOException("Can't create directory (" + targetFile.getParentFile() + ")");
        }

        InputStream in = buildInputStream(connection.getInputStream());
        OutputStream fileOut = new FileOutputStream(targetFile);

        byte[] buffer = new byte[bufferSize];
        int length;

        while (running && ((length = in.read(buffer)) != -1))
        {
            fileOut.write(buffer, 0, length);
            downloaded += length;

            if ((size != -1) && (percentage < (percentage = (int) (downloaded * 100 / size))))
            {
                onPercentageChange(targetFile, percentage, size, downloaded);
            }
        }

        if (size == -1)
        {
            percentage = 100;
            downloaded = size;
            onPercentageChange(targetFile, percentage, size, size);
        }

        fileOut.close();

        running = false;
    }

    public URL getInputUrl()
    {
        return inputUrl;
    }

    protected InputStream buildInputStream(InputStream parent) throws IOException
    {
        return parent;
    }

    @Override
    public void stop()
    {
        running = false;
    }

    @Override
    public boolean isRunning()
    {
        return running;
    }

    @Override
    public File getCurrentFile()
    {
        return targetFile;
    }

    @Override
    public int getOverallPercentage()
    {
        return percentage;
    }

    @Override
    public long getCurrentFileSize()
    {
        return size;
    }

    @Override
    public long getCurrentDownloadedSize()
    {
        return downloaded;
    }

    @Override
    public void registerCallback(DownloaderCallback callback)
    {
        callbackList.add(callback);
    }

    @Override
    public void removeCallback(DownloaderCallback callback)
    {
        callbackList.remove(callback);
    }

    private void onPercentageChange(File file, int percentage, long size, long downloaded)
    {
        for (DownloaderCallback callback : callbackList)
        {
            callback.onPercentageChange(file, percentage, size, downloaded);
        }
    }

    @Override
    public File getFile()
    {
        return targetFile;
    }
}
