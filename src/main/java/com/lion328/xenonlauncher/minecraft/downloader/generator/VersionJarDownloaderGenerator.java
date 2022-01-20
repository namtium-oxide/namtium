// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.downloader.generator;

import com.lion328.xenonlauncher.downloader.Downloader;
import com.lion328.xenonlauncher.downloader.DownloaderGenerator;
import com.lion328.xenonlauncher.downloader.FileDownloader;
import com.lion328.xenonlauncher.downloader.URLFileDownloader;
import com.lion328.xenonlauncher.downloader.VerifiyFileDownloader;
import com.lion328.xenonlauncher.downloader.verifier.MessageDigestFileVerifier;
import com.lion328.xenonlauncher.minecraft.downloader.verifier.MinecraftFileVerifier;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.DownloadInformation;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.GameVersion;
import com.lion328.xenonlauncher.util.URLUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class VersionJarDownloaderGenerator implements DownloaderGenerator {

    public static final URL DEFAULT_MINECRAFT_VERSIONS = URLUtil.constantURL("https://s3.amazonaws.com/Minecraft.Download/versions/");

    private final GameVersion info;
    private final File file;
    private final URL downloadURL;

    public VersionJarDownloaderGenerator(GameVersion info, File file) throws MalformedURLException {
        this(info, file, DEFAULT_MINECRAFT_VERSIONS);
    }

    public VersionJarDownloaderGenerator(GameVersion info, File file, URL downloadURL) throws MalformedURLException {
        this.info = info;
        this.file = file;
        this.downloadURL = downloadURL;
    }

    public GameVersion getGameVersion() {
        return info;
    }

    public File getFile() {
        return file;
    }

    @Override
    public List<Downloader> generateDownloaders() throws IOException {
        URL jarURL = null;
        FileDownloader downloader;

        if (info.getDownloadsInformation() != null) {
            DownloadInformation downloadInfo = info.getDownloadsInformation().get(GameVersion.DOWNLOAD_CLIENT);
            jarURL = downloadInfo.getURL();
            downloader = new URLFileDownloader(jarURL, file);
            downloader = new VerifiyFileDownloader(downloader, new MinecraftFileVerifier(downloadInfo));
        } else {
            jarURL = new URL(downloadURL, info.getJarName() + "/" + info.getJarName() + ".jar");

            String md5 = URLUtil.getETag(jarURL).trim().replace("\"", "");

            downloader = new URLFileDownloader(jarURL, file);
            downloader = new VerifiyFileDownloader(downloader, new MessageDigestFileVerifier(MessageDigestFileVerifier.MD5, md5));
        }

        return Collections.singletonList((Downloader) downloader);
    }
}
