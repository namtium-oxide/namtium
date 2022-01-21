// Copyright (C) 2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.launcher.hydra;

import java.io.File;
import java.util.regex.Pattern;

public interface CrashReportUI {
    Pattern CRASH_REPORT_REGEX = Pattern.compile("^.*#@!@#.*Crash report saved to: #@!@# (.*)$");

    void onGameCrash(final File crashReportFile);
}
