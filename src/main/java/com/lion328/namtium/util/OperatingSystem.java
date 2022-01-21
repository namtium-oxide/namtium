// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.util;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public enum OperatingSystem {

    @SerializedName("win") WINDOWS("win"),
    @SerializedName("osx") OSX("osx"),
    @SerializedName("linux") LINUX("linux");

    private static OperatingSystem currentOS = null;
    private static Architecture currentArch = null;
    private static File appdata = null;
    private final String s;

    OperatingSystem(String s) {
        this.s = s;
    }

    public static OperatingSystem fromString(String s) {
        for (OperatingSystem os : values()) {
            if (os.toString().equals(s)) {
                return os;
            }
        }
        return null;
    }

    public static OperatingSystem getCurrentOS() {
        if (currentOS == null) {
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("win")) {
                currentOS = OperatingSystem.WINDOWS;
            } else if (osName.contains("mac")) {
                currentOS = OperatingSystem.OSX;
            } else {
                currentOS = OperatingSystem.LINUX;
            }
        }
        return currentOS;
    }

    public static String getCurrentVersion() {
        return System.getProperty("os.version");
    }

    public static Architecture getCurrentArchitecture() {
        if (currentArch == null) {
            currentArch = Architecture.fromString(System.getProperty("sun.arch.data.model"));
        }

        return currentArch;
    }

    public static String getCurrentArchitectureName() {
        return getCurrentArchitecture().toString();
    }

    public static File getApplicationDataDirectory() {
        if (appdata == null) {
            File home = new File(System.getProperty("user.home", "."));

            switch (getCurrentOS()) {
                default:
                case LINUX:
                    appdata = home;
                    break;
                case WINDOWS:
                    String winAppdata = System.getenv("APPDATA");

                    if (winAppdata == null) {
                        appdata = home;
                    } else {
                        appdata = new File(winAppdata);
                    }

                    break;
                case OSX:
                    appdata = new File(home, "Library/Application Support/");
                    break;
            }
        }
        return appdata;
    }

    public String toString() {
        return s;
    }

    public enum Architecture {

        ARCH_32("32"), ARCH_64("64"), UNKNOWN("unknown");

        private String s;

        Architecture(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return s;
        }

        public static Architecture fromString(String s) {
            for (Architecture arch : values()) {
                if (arch.s.equals(s)) {
                    return arch;
                }
            }

            return UNKNOWN;
        }
    }
}
