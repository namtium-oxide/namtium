// Copyright (C) 2018-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium;

import com.lion328.hydra.HydraSettings;
import com.lion328.xenonlauncher.util.URLUtil;

import java.net.URL;
import java.util.UUID;

public class Settings implements HydraSettings {

    private static Settings instance;

    private static final URL launcherBaseURL = URLUtil.constantURL("http://example.com/launcher/");
    private static final URL registerURL = URLUtil.constantURL("http://example.com/register");
    private static final URL authURL = fromBase("api/authenticate.php");
    private static final URL whitelistURL = fromBase("whitelist");
    private static final URL compressedFilesURL = fromBase("filelist.gz");
    private static final URL filesURL = fromBase("files/");

    private Settings() {

    }

    @Override
    public URL getRegisterURL() {
        return registerURL;
    }

    @Override
    public URL getAuthenticationURL() {
        return authURL;
    }

    @Override
    public URL getWhitelistFileListURL() {
        return whitelistURL;
    }

    @Override
    public URL getCompressedFileListURL() {
        return compressedFilesURL;
    }

    @Override
    public URL getFileBaseURL() {
        return filesURL;
    }

    @Override
    public UUID getApplicationUUID() {
        return UUID.fromString("769206ae-9c87-4aa7-ad47-7e939679ba37");
    }

    @Override
    public String getGameVersionName() {
        return "game";
    }

    @Override
    public String[] getVMArguments() {
        return new String[]{
                "-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump",
                "-XX:+UseConcMarkSweepGC",
                "-XX:+CMSIncrementalMode",
                "-XX:-UseAdaptiveSizePolicy"
        };
    }

    @Override
    public boolean streamGameOutput() {
        return true;
    }

    @Override
    public boolean passPasswordToGame() {
        return false;
    }

    @Override
    public int getMinimumOfMaximumMemoryInMiB() {
        return 512;
    }

    @Override
    public int getDefaultMaximumMemoryInMiB() {
        return 1024;
    }

    private static URL fromBase(String s) {
        return URLUtil.constantURL(launcherBaseURL.toString() + s);
    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }

        return instance;
    }
}
