// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.hydra;

import com.lion328.xenonlauncher.downloader.DownloaderCallback;
import com.lion328.xenonlauncher.downloader.FileDownloader;
import com.lion328.xenonlauncher.util.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeleteFileDownloader implements FileDownloader {

    private final File file;
    private final boolean recursive;
    private final List<DownloaderCallback> callbackList;
    private boolean running;
    private int percentage;

    public DeleteFileDownloader(File file, boolean recursive) {
        this.file = file;
        this.recursive = recursive;

        callbackList = new ArrayList<>();
        running = false;
        percentage = 0;
    }

    private boolean deleteFile() {
        if (recursive) {
            return FileUtil.deleteFileRescursive(file);
        }

        return file.delete();
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void download() throws IOException {
        running = true;

        if (file.exists() && !deleteFile()) {
            running = false;

            throw new IOException("Can't delete file (" + file.getAbsolutePath() + ")");
        }

        percentage = 100;

        for (DownloaderCallback callback : callbackList) {
            callback.onPercentageChange(file, percentage, -1, -1);
        }

        percentage = 0;

        running = false;
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public File getCurrentFile() {
        return file;
    }

    @Override
    public int getOverallPercentage() {
        return percentage;
    }

    @Override
    public long getCurrentFileSize() {
        return -1;
    }

    @Override
    public long getCurrentDownloadedSize() {
        return -1;
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
