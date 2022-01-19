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

import com.lion328.xenonlauncher.downloader.verifier.FileVerifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VerifiyFileDownloader implements FileDownloader
{

    private final FileDownloader downloader;
    private final FileVerifier verifier;
    private final List<DownloaderCallback> callbackList;
    private final DownloaderCallback callback;

    private int percentage;
    private long size;
    private long downloaded;

    public VerifiyFileDownloader(FileDownloader downloader, FileVerifier verifier)
    {
        this.downloader = downloader;
        this.verifier = verifier;

        callback = new DownloaderCallback()
        {

            @Override
            public void onPercentageChange(File file, int overallPercentage, long fileSize, long fileDownloaded)
            {
                for (DownloaderCallback callback : callbackList)
                {
                    callback.onPercentageChange(file, overallPercentage, fileSize, fileDownloaded);
                }
            }
        };

        callbackList = new ArrayList<>();

        reset();
    }

    private void reset()
    {
        percentage = 0;
        size = -1;
        downloaded = -1;
    }

    private void runCallback()
    {
        callback.onPercentageChange(downloader.getFile(), percentage, size, downloaded);
    }

    @Override
    public synchronized void download() throws IOException
    {
        if (downloader.isRunning())
        {
            return;
        }

        reset();
        runCallback();

        if (verifier.isValid(getFile()))
        {
            return;
        }

        downloader.registerCallback(callback);
        downloader.download();
        downloader.removeCallback(callback);
    }

    @Override
    public void stop()
    {
        downloader.stop();
    }

    @Override
    public boolean isRunning()
    {
        return downloader.isRunning();
    }

    @Override
    public File getCurrentFile()
    {
        return downloader.getCurrentFile();
    }

    @Override
    public int getOverallPercentage()
    {
        return downloader.getOverallPercentage();
    }

    @Override
    public long getCurrentFileSize()
    {
        return downloader.getCurrentFileSize();
    }

    @Override
    public long getCurrentDownloadedSize()
    {
        return downloader.getCurrentDownloadedSize();
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

    @Override
    public File getFile()
    {
        return downloader.getFile();
    }
}
