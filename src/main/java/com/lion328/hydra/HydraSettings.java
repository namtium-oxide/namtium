// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.hydra;

import java.net.URL;
import java.util.UUID;

public interface HydraSettings {

    URL getRegisterURL();

    URL getAuthenticationURL();

    URL getWhitelistFileListURL();

    URL getCompressedFileListURL();

    URL getFileBaseURL();

    UUID getApplicationUUID();

    String getGameVersionName();

    String[] getVMArguments();

    boolean streamGameOutput();

    boolean passPasswordToGame();

    int getMinimumOfMaximumMemoryInMiB();

    int getDefaultMaximumMemoryInMiB();
}
