// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium;

import com.lion328.namtium.generated.LauncherVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Namtium {

    public static final Logger LOGGER;
    public static final String NAME = "namtium";
    public static final String VERSION = LauncherVersion.VERSION;

    static {
        LOGGER = LoggerFactory.getLogger(NAME);
    }
}
