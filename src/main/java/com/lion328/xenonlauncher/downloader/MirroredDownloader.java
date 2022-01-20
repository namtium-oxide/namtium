// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.downloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MirroredDownloader implements Downloader
{

    private final List<Downloader> mirrors;
    private final List<DownloaderCallback> callbackList;

    private Downloader workingDownloader;

    public MirroredDownloader(Downloader main, Downloader mirror)
    {
        this(new ArrayList<Downloader>());

        List<Downloader> list = getDownloaders();

        list.add(main);
        list.add(mirror);
    }

    public MirroredDownloader(List<Downloader> mirrors)
    {
        this.callbackList = new ArrayList<>();
        this.mirrors = mirrors;

        DownloaderCallback callback = new DownloaderCallback()
        {

            @Override
            public void onPercentageChange(File file, int overallPercentage, long fileSize, long fileDownloaded)
            {
                for (DownloaderCallback callback : callbackList)
                {
                    onPercentageChange(file, overallPercentage, fileSize, fileDownloaded);
                }
            }
        };


        for (Downloader downloader : mirrors)
        {
            downloader.registerCallback(callback);
        }
    }

    public List<Downloader> getDownloaders()
    {
        return mirrors;
    }

    @Override
    public void download() throws IOException
    {
        boolean success = false;
        IOException lastException = null;

        for (Downloader downloader : mirrors)
        {
            try
            {
                workingDownloader = downloader;

                downloader.download();

                success = true;
                break;
            }
            catch (IOException e)
            {
                lastException = e;
            }
        }

        workingDownloader = null;

        if (!success)
        {
            throw lastException;
        }
    }

    @Override
    public void stop()
    {
        if (workingDownloader != null)
        {
            workingDownloader.stop();
        }
    }

    @Override
    public boolean isRunning()
    {
        return workingDownloader != null && workingDownloader.isRunning();
    }

    @Override
    public File getCurrentFile()
    {
        if (workingDownloader != null)
        {
            return workingDownloader.getCurrentFile();
        }

        return null;
    }

    @Override
    public int getOverallPercentage()
    {
        if (workingDownloader != null)
        {
            return workingDownloader.getOverallPercentage();
        }

        return 0;
    }

    @Override
    public long getCurrentFileSize()
    {
        if (workingDownloader != null)
        {
            return workingDownloader.getCurrentFileSize();
        }

        return 0;
    }

    @Override
    public long getCurrentDownloadedSize()
    {
        if (workingDownloader != null)
        {
            return workingDownloader.getCurrentDownloadedSize();
        }

        return 0;
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
