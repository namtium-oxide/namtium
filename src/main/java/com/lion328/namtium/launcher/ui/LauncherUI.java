// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.launcher.ui;

import com.lion328.namtium.downloader.DownloaderCallback;
import com.lion328.namtium.launcher.Launcher;

public interface LauncherUI extends DownloaderCallback {

    void start();

    Launcher getLauncher();

    void setLauncher(Launcher launcher);

    boolean isVisible();

    void setVisible(boolean visible);

    void displayError(String message);
}
