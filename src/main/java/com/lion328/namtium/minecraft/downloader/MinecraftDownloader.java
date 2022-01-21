// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.minecraft.downloader;

import com.lion328.namtium.downloader.Downloader;
import com.lion328.namtium.downloader.DownloaderCallback;
import com.lion328.namtium.downloader.DownloaderGenerator;
import com.lion328.namtium.downloader.FileDownloader;
import com.lion328.namtium.downloader.MultipleDownloader;
import com.lion328.namtium.downloader.URLFileDownloader;
import com.lion328.namtium.downloader.VerifiyFileDownloader;
import com.lion328.namtium.downloader.repository.Repository;
import com.lion328.namtium.downloader.verifier.FileVerifier;
import com.lion328.namtium.downloader.verifier.MessageDigestFileVerifier;
import com.lion328.namtium.minecraft.StartupConfiguration;
import com.lion328.namtium.minecraft.assets.Assets;
import com.lion328.namtium.minecraft.downloader.generator.AssetsDownloaderGenerator;
import com.lion328.namtium.minecraft.downloader.generator.LibraryDownloaderGenerator;
import com.lion328.namtium.minecraft.downloader.generator.VersionJarDownloaderGenerator;
import com.lion328.namtium.minecraft.manifest.AssetInformation;
import com.lion328.namtium.minecraft.manifest.GameLibrary;
import com.lion328.namtium.minecraft.manifest.GameVersion;
import com.lion328.namtium.minecraft.manifest.MergedGameVersion;
import com.lion328.namtium.minecraft.manifest.gson.GsonFactory;
import com.lion328.namtium.util.URLUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MinecraftDownloader implements Downloader {

    private final String id;
    private final File basepath;
    private final GameVersionList gameVersionList;
    private final URL assetsURL;
    private final URL assetsIndexesURL;
    private final Repository repository;
    private final StartupConfiguration startupConfiguration;
    private final Downloader additionalDownloader;

    private final List<DownloaderCallback> callbackList;
    private final DownloaderCallback unmodifiedProgressCallback;
    private final DownloaderCallback callback;
    private final File versionsDir;
    private final File librariesDir;
    private final File versionDir;

    private boolean running;
    private int overallPercentage;
    private File currentFile;
    private long fileSize;
    private long fileDownloaded;

    public MinecraftDownloader(String id, File basepath, GameVersionList gameVersionList, Repository repository, Downloader additionalDownloader) {
        this(id, basepath, gameVersionList, AssetsDownloaderGenerator.DEFAULT_ASSETS_URL, AssetsDownloaderGenerator.DEFAULT_ASSETS_INDEXES_URL, repository, additionalDownloader);
    }

    public MinecraftDownloader(String id, File basepath, GameVersionList gameVersionList, URL assetsURL, URL assetsIndexesURL, Repository repository, Downloader additionalDownloader) {
        this(id, basepath, gameVersionList, assetsURL, assetsIndexesURL, repository, StartupConfiguration.getRunningConfig(), additionalDownloader);
    }

    public MinecraftDownloader(String id, File basepath, GameVersionList gameVersionList, URL assetsURL, URL assetsIndexesURL, Repository repository, StartupConfiguration startupConfiguration, Downloader additionalDownloader) {
        this.id = id;
        this.basepath = basepath;
        this.gameVersionList = gameVersionList;
        this.assetsURL = assetsURL;
        this.assetsIndexesURL = assetsIndexesURL;
        this.repository = repository;
        this.startupConfiguration = startupConfiguration;
        this.additionalDownloader = additionalDownloader;

        callbackList = new ArrayList<>();

        final MinecraftDownloader self = this;

        unmodifiedProgressCallback = new DownloaderCallback() {
            @Override
            public void onPercentageChange(File file, int overallPercentage, long fileSize, long fileDownloaded) {
                self.currentFile = file;
                self.fileSize = fileSize;
                self.fileDownloaded = fileDownloaded;

                self.callCallbacks();
            }
        };

        callback = new DownloaderCallback() {

            @Override
            public void onPercentageChange(File file, int overallPercentage, long fileSize, long fileDownloaded) {
                self.overallPercentage = overallPercentage;
                unmodifiedProgressCallback.onPercentageChange(file, overallPercentage, fileSize, fileDownloaded);
            }
        };

        versionsDir = new File(basepath, "versions");
        librariesDir = new File(basepath, "libraries");
        versionDir = new File(versionsDir, id);
    }

    private void callCallbacks() {
        for (DownloaderCallback callback : callbackList) {
            callback.onPercentageChange(currentFile, overallPercentage, fileSize, fileDownloaded);
        }
    }

    private Object downloadJson(File file, URL url, FileVerifier verifier, Class<?> clazz) throws IOException {
        FileDownloader downloader = new URLFileDownloader(url, file);
        downloader = new VerifiyFileDownloader(downloader, verifier);

        downloader.registerCallback(unmodifiedProgressCallback);
        downloader.download();

        return GsonFactory.create().fromJson(new InputStreamReader(new FileInputStream(file)), clazz);
    }

    private GameVersion downloadGameVersion(String id) throws IOException {
        File versionJson = new File(versionsDir, id + "/" + id + ".json");
        URL versionJsonUrl = gameVersionList.getVersionManifestURL(id);
        FileVerifier verifier = new MessageDigestFileVerifier(MessageDigestFileVerifier.MD5, URLUtil.getETag(versionJsonUrl).trim().replace("\"", ""));

        GameVersion versionInfo = (GameVersion) downloadJson(versionJson, versionJsonUrl, verifier, GameVersion.class);

        if (versionInfo.getParentID() != null) {
            versionInfo = new MergedGameVersion(versionInfo, downloadGameVersion(versionInfo.getParentID()));
        }

        return versionInfo;
    }

    private Assets downloadAssetsInfomation(GameVersion version) throws IOException {
        Assets info;
        File assetsFile = new File(basepath, "assets/indexes/" + version.getAssets() + ".json");
        AssetInformation assetInfo = version.getAssetInformation();
        URL url;
        FileVerifier verifier;

        if (assetInfo != null) {
            url = assetInfo.getURL();
        } else {
            url = new URL(assetsIndexesURL, version.getAssets() + ".json");
        }

        if (assetInfo != null && assetInfo.isKnownSizeAndHash()) {
            verifier = new MessageDigestFileVerifier(MessageDigestFileVerifier.SHA_1, assetInfo.getSHA1Hash());
        } else {
            verifier = new MessageDigestFileVerifier(MessageDigestFileVerifier.MD5, URLUtil.getETag(url).replace("\"", ""));
        }

        return (Assets) downloadJson(assetsFile, url, verifier, Assets.class);
    }

    @Override
    public synchronized void download() throws IOException {
        if (running) {
            return;
        }

        running = true;

        overallPercentage = 0;

        GameVersion versionInfo = downloadGameVersion(id);

        List<Downloader> downloaders = new ArrayList<>();

        File librariesDir = new File(basepath, "libraries");
        File assetsObjectsDir = new File(basepath, "assets/objects");

        DownloaderGenerator generator = new VersionJarDownloaderGenerator(versionInfo, new File(versionDir, versionInfo.getJarName() + ".jar"));
        downloaders.addAll(generator.generateDownloaders());

        for (GameLibrary library : versionInfo.getLibraries()) {
            if (!library.isAllowed(startupConfiguration)) {
                continue;
            }

            generator = new LibraryDownloaderGenerator(library, librariesDir, startupConfiguration.getOperatingSystem(), startupConfiguration.getArchitecture(), repository);
            downloaders.addAll(generator.generateDownloaders());
        }

        generator = new AssetsDownloaderGenerator(downloadAssetsInfomation(versionInfo), assetsObjectsDir, assetsURL);
        downloaders.addAll(generator.generateDownloaders());

        if (additionalDownloader != null) {
            downloaders.add(additionalDownloader);
        }

        Downloader downloader = new MultipleDownloader(downloaders);
        downloader.registerCallback(callback);
        downloader.download();

        running = false;
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public File getCurrentFile() {
        return currentFile;
    }

    @Override
    public int getOverallPercentage() {
        return overallPercentage;
    }

    @Override
    public long getCurrentFileSize() {
        return fileSize;
    }

    @Override
    public long getCurrentDownloadedSize() {
        return fileDownloaded;
    }

    @Override
    public void registerCallback(DownloaderCallback callback) {
        callbackList.add(callback);
    }

    @Override
    public void removeCallback(DownloaderCallback callback) {
        callbackList.remove(callback);
    }
}
