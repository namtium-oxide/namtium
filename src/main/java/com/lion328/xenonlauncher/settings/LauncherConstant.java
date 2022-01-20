// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.settings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LauncherConstant {

    public static final Logger LOGGER;
    public static final String NAME = "xenonlauncher";
    public static final String VERSION = "0.3"; // TODO: update in build.gradle

    static {
        LOGGER = LogManager.getLogger(NAME);
    }
}
