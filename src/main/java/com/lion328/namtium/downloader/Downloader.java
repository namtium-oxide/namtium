// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.downloader;

import java.io.IOException;

public interface Downloader extends DownloaderCallbackHandler {

    void download() throws IOException;

    void stop();

    boolean isRunning();
}
