// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.util.io;

import java.io.File;
import java.nio.file.Files;

public class FileUtil {

    public static boolean deleteFileRescursive(File file) {
        return deleteFileRescursive(file, true);
    }

    public static boolean deleteFileRescursive(File file, boolean safeDelete) {
        String path = file.getAbsolutePath();
        if (safeDelete && (path.equals("/") || path.equals(System.getProperty("user.home")))) {
            throw new RuntimeException("Did you really trying to remove entire filesystem? or user's home directory?");
        }

        if (!file.exists()) {
            return true;
        }

        if (file.isFile() || Files.isSymbolicLink(file.toPath())) {
            return file.delete();
        }

        File[] files = file.listFiles();
        if (files == null) {
            return false;
        }

        for (File f : files) {
            if (!deleteFileRescursive(f)) {
                return false;
            }
        }

        return file.delete();
    }
}
