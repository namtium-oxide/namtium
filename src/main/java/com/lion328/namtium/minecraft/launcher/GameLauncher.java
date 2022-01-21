// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.minecraft.launcher;

import com.lion328.namtium.minecraft.authentication.UserInformation;

import java.io.File;

public interface GameLauncher {

    Process launch() throws Exception;

    File getGameDirectory();

    void setGameDirectory(File dir);

    void setVariable(String key, String value);

    void addJVMArgument(String arg);

    void addGameArgument(String key, String value);

    void addGameArgument(String arg);

    void setMaxMemorySize(int mb);

    void setUserInformation(UserInformation profile);

    void setFeature(String key, boolean value);

    Boolean getFeature(String key);

    void removeFeature(String key);
}
