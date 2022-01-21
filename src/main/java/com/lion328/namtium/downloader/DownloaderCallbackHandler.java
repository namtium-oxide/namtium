// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.downloader;

import java.io.File;

public interface DownloaderCallbackHandler {

    File getCurrentFile();

    int getOverallPercentage();

    long getCurrentFileSize();

    long getCurrentDownloadedSize();

    void registerCallback(DownloaderCallback callback);

    void removeCallback(DownloaderCallback callback);
}
