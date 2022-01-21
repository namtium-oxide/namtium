// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.downloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultipleDownloader implements Downloader {

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

    public MultipleDownloader(List<Downloader> downloaders) {
        this(downloaders, DEFAULT_RETIRES);
    }

    public MultipleDownloader(List<Downloader> downloaders, int retries) {
        this.downloaderList = Collections.unmodifiableList(downloaders);
        this.retries = retries;

        callbackList = new ArrayList<>();

        callback = new DownloaderCallback() {

            @Override
            public void onPercentageChange(File file, int overallPercentage, long fileSize, long fileDownloaded) {
                currentFile = file;
                overall = ((idx * 100 + overallPercentage) / downloaderList.size()); // ((idx + progress / 100) / size) * 100
                size = fileSize;
                downloaded = fileDownloaded;

                callCallbacks();
            }
        };
    }

    public List<Downloader> getList() {
        return downloaderList;
    }

    private void callCallbacks() {
        for (DownloaderCallback callback : callbackList) {
            callback.onPercentageChange(currentFile, overall, size, downloaded);
        }
    }

    @Override
    public void download() throws IOException {
        if (running) {
            return;
        }

        running = true;
        idx = 0;

        for (Downloader downloader : downloaderList) {
            if (downloader == null) {
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
    public void stop() {
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public File getCurrentFile() {
        return currentFile;
    }

    @Override
    public int getOverallPercentage() {
        return overall;
    }

    @Override
    public long getCurrentFileSize() {
        return size;
    }

    @Override
    public long getCurrentDownloadedSize() {
        return downloaded;
    }

    @Override
    public void registerCallback(DownloaderCallback callback) {
        callbackList.add(callback);
    }

    @Override
    public void removeCallback(DownloaderCallback callback) {
        callbackList.remove(callback);
    }
}
