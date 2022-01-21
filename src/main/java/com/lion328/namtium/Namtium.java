// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium;

import com.lion328.namtium.generated.LauncherVersion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Namtium {

    public static final Logger LOGGER;
    public static final String NAME = "namtium";
    public static final String VERSION = LauncherVersion.VERSION;

    static {
        LOGGER = LogManager.getLogger(NAME);
    }
}
