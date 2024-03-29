// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.launcher;

import com.lion328.namtium.launcher.ui.LauncherUI;
import com.lion328.namtium.minecraft.authentication.UserInformation;

import java.io.IOException;

public interface Launcher {

    void start();

    UserInformation getCacheUser() throws IOException;

    void setCacheUser(UserInformation userInfo) throws IOException;

    void clearCacheUser() throws IOException;

    LauncherUI getLauncherUI();

    void setLauncherUI(LauncherUI ui);

    boolean loginAndLaunch(String username, char[] password);

    void exit();
}
