// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.launcher;

import com.lion328.namtium.util.OperatingSystem;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Util {

    private static char[] unitTable = new char[]{'\0', 'K', 'M', 'G', 'T'};
    private static File workingJar;

    public static String convertUnit(long l) {
        int unit = 0;
        float f = l;

        while (f >= 1024 && (unit + 1 <= unitTable.length)) {
            f *= 0.0009765625F; // 1 / 1024
            unit++;
        }

        if (unit == 0) {
            return String.valueOf(l);
        }

        return String.format("%.2f", f) + " " + unitTable[unit] + "i";
    }

    public static File getWorkingJar() {
        if (workingJar == null) {
            try {
                workingJar = new File(Util.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            } catch (URISyntaxException e) {
                HydraLauncher.getLogger().catching(e);
            }
        }

        return workingJar;
    }

    public static File getGameDirectory(String serverName) {
        return new File(OperatingSystem.getApplicationDataDirectory(),
                HydraLauncher.LAUNCHER_DIRECTORY_NAME + File.separator + serverName);
    }

    public static void openURL(URL url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(url.toURI());
            } catch (IOException | URISyntaxException e) {
                HydraLauncher.getLogger().catching(e);
            }
        }
    }
}
