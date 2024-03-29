// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.downloader;

import java.io.File;

public interface DownloaderCallback {

    void onPercentageChange(File file, int overallPercentage, long fileSize, long fileDownloaded);
}
