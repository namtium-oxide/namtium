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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultipleDownloader implements Downloader
{

    public static final int DEFAULT_RETIRES = 5;

    private final List<Downloader> downloaderList;
    private final int retries;

    private final List<DownloaderCallback> callbackList;
    private final DownloaderCallback callback;

    private int idx;
    private boolean running;
    private int overall;
    private File currentFile;
    private long size;
    private long downloaded;

    public MultipleDownloader(List<Downloader> downloaders)
    {
        this(downloaders, DEFAULT_RETIRES);
    }

    public MultipleDownloader(List<Downloader> downloaders, int retries)
    {
        this.downloaderList = Collections.unmodifiableList(downloaders);
        this.retries = retries;

        callbackList = new ArrayList<>();

        callback = new DownloaderCallback()
        {

            @Override
            public void onPercentageChange(File file, int overallPercentage, long fileSize, long fileDownloaded)
            {
                currentFile = file;
                overall = ((idx * 100 + overallPercentage) / downloaderList.size()); // ((idx + progress / 100) / size) * 100
                size = fileSize;
                downloaded = fileDownloaded;

                callCallbacks();
            }
        };
    }

    public List<Downloader> getList()
    {
        return downloaderList;
    }

    private void callCallbacks()
    {
        for (DownloaderCallback callback : callbackList)
        {
            callback.onPercentageChange(currentFile, overall, size, downloaded);
        }
    }

    @Override
    public void download() throws IOException
    {
        if (running)
        {
            return;
        }

        running = true;
        idx = 0;

        for (Downloader downloader : downloaderList)
        {
            if (downloader == null)
            {
                continue;
            }

            downloader.registerCallback(callback);
            downloader.download();
            downloader.removeCallback(callback);

            idx++;
        }

        overall = 100;
        callCallbacks();

        running = false;
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
        return currentFile;
    }

    @Override
    public int getOverallPercentage()
    {
        return overall;
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
}
