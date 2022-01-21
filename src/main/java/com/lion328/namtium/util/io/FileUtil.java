// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.util.io;

import com.lion328.namtium.launcher.hydra.HydraLauncher;
import com.lion328.namtium.util.OperatingSystem;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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

    public static List<File> listFiles(File directory, boolean withDirectory) {
        List<File> fileList = new ArrayList<>();

        if (!directory.exists()) {
            return null;
        }

        File[] files = directory.listFiles();

        if (files == null) {
            return null;
        }

        List<File> tmp;

        for (File file : files) {
            if (file.isDirectory()) {
                tmp = listFiles(file, withDirectory);

                if (tmp != null) {
                    fileList.addAll(tmp);
                }

                if (withDirectory) {
                    fileList.add(file);
                }
            } else {
                fileList.add(file);
            }
        }

        return fileList;
    }

    public static File getGameDirectory(String serverName) {
        return new File(OperatingSystem.getApplicationDataDirectory(),
                HydraLauncher.LAUNCHER_DIRECTORY_NAME + File.separator + serverName);
    }
}
