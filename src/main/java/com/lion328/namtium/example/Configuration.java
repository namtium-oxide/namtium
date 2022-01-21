// Copyright (C) 2018-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.example;

import com.lion328.namtium.launcher.hydra.config.HydraConfiguration;
import com.lion328.namtium.util.URLUtil;

import java.net.URL;
import java.util.UUID;

public class Configuration implements HydraConfiguration {

    private static Configuration instance;

    private static final URL launcherBaseURL = URLUtil.constantURL("http://example.com/launcher/");
    private static final URL registerURL = URLUtil.constantURL("http://example.com/register");
    private static final URL authURL = fromBase("api/authenticate.php");
    private static final URL whitelistURL = fromBase("whitelist");
    private static final URL compressedFilesURL = fromBase("filelist.gz");
    private static final URL filesURL = fromBase("files/");

    private Configuration() {

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
        return new String[]{};
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

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }

        return instance;
    }
}
