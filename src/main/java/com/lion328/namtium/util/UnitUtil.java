// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.util;

import com.lion328.namtium.launcher.hydra.HydraLauncher;

import java.io.File;
import java.net.URISyntaxException;

public class UnitUtil {

    private static char[] unitTable = new char[]{'\0', 'K', 'M', 'G', 'T'};

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
}
