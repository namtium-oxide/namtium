/*
 * Copyright (c) 2018 Waritnan Sookbuntherng
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.lion328.xenonlauncher.minecraft.downloader;

import com.lion328.xenonlauncher.downloader.Downloader;
import com.lion328.xenonlauncher.downloader.DownloaderCallback;
import com.lion328.xenonlauncher.downloader.DownloaderGenerator;
import com.lion328.xenonlauncher.downloader.FileDownloader;
import com.lion328.xenonlauncher.downloader.MultipleDownloader;
import com.lion328.xenonlauncher.downloader.URLFileDownloader;
import com.lion328.xenonlauncher.downloader.VerifiyFileDownloader;
import com.lion328.xenonlauncher.downloader.repository.Repository;
import com.lion328.xenonlauncher.downloader.verifier.FileVerifier;
import com.lion328.xenonlauncher.downloader.verifier.MessageDigestFileVerifier;
import com.lion328.xenonlauncher.minecraft.assets.data.Assets;
import com.lion328.xenonlauncher.minecraft.assets.downloader.AssetsDownloaderGenerator;
import com.lion328.xenonlauncher.minecraft.downloader.generator.LibraryDownloaderGenerator;
import com.lion328.xenonlauncher.minecraft.downloader.generator.VersionJarDownloaderGenerator;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.AssetInformation;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.GameLibrary;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.GameVersion;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.MergedGameVersion;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.gson.GsonFactory;
import com.lion328.xenonlauncher.util.OS;
import com.lion328.xenonlauncher.util.URLUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MinecraftDownloader implements Downloader
{

    private final String id;
    private final File basepath;
    private final URL gameURL;
    private final URL assetsURL;
    private final URL assetsIndexesURL;
    private final Repository repository;
    private final OS os;
    private final String osVersion;
    private final OS.Architecture osArch;
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

    public MinecraftDownloader(String id, File basepath, Repository repository, Downloader additionalDownloader)
    {
        this(id, basepath, VersionJarDownloaderGenerator.DEFAULT_MINECRAFT_VERSIONS, AssetsDownloaderGenerator.DEFAULT_ASSETS_URL, AssetsDownloaderGenerator.DEFAULT_ASSETS_INDEXES_URL, repository, additionalDownloader);
    }

    public MinecraftDownloader(String id, File basepath, URL gameURL, URL assetsURL, URL assetsIndexesURL, Repository repository, Downloader additionalDownloader)
    {
        this(id, basepath, gameURL, assetsURL, assetsIndexesURL, repository, OS.getCurrentOS(), OS.getCurrentVersion(), OS.getCurrentArchitecture(), additionalDownloader);
    }

    public MinecraftDownloader(String id, File basepath, URL gameURL, URL assetsURL, URL assetsIndexesURL, Repository repository, OS os, String osVersion, OS.Architecture osArch, Downloader additionalDownloader)
    {
        this.id = id;
        this.basepath = basepath;
        this.gameURL = gameURL;
        this.assetsURL = assetsURL;
        this.assetsIndexesURL = assetsIndexesURL;
        this.repository = repository;
        this.os = os;
        this.osVersion = osVersion;
        this.osArch = osArch;
        this.additionalDownloader = additionalDownloader;

        callbackList = new ArrayList<>();

        final MinecraftDownloader self = this;

        unmodifiedProgressCallback = new DownloaderCallback()
        {
            @Override
            public void onPercentageChange(File file, int overallPercentage, long fileSize, long fileDownloaded)
            {
                self.currentFile = file;
                self.fileSize = fileSize;
                self.fileDownloaded = fileDownloaded;

                self.callCallbacks();
            }
        };

        callback = new DownloaderCallback()
        {

            @Override
            public void onPercentageChange(File file, int overallPercentage, long fileSize, long fileDownloaded)
            {
                self.overallPercentage = overallPercentage;
                unmodifiedProgressCallback.onPercentageChange(file, overallPercentage, fileSize, fileDownloaded);
            }
        };

        versionsDir = new File(basepath, "versions");
        librariesDir = new File(basepath, "libraries");
        versionDir = new File(versionsDir, id);
    }

    private void callCallbacks()
    {
        for (DownloaderCallback callback : callbackList)
        {
            callback.onPercentageChange(currentFile, overallPercentage, fileSize, fileDownloaded);
        }
    }

    private Object downloadJson(File file, URL url, FileVerifier verifier, Class<?> clazz) throws IOException
    {
        FileDownloader downloader = new URLFileDownloader(url, file);
        downloader = new VerifiyFileDownloader(downloader, verifier);

        downloader.registerCallback(unmodifiedProgressCallback);
        downloader.download();

        return GsonFactory.create().fromJson(new InputStreamReader(new FileInputStream(file)), clazz);
    }

    private GameVersion downloadGameVersion(String id) throws IOException
    {
        String path = id + "/" + id + ".json";
        File versionJson = new File(versionsDir, path);
        URL versionJsonUrl = new URL(gameURL, path);
        FileVerifier verifier = new MessageDigestFileVerifier(MessageDigestFileVerifier.MD5, URLUtil.getETag(versionJsonUrl).trim().replace("\"", ""));

        GameVersion versionInfo = (GameVersion) downloadJson(versionJson, versionJsonUrl, verifier, GameVersion.class);

        if (versionInfo.getParentID() != null)
        {
            versionInfo = new MergedGameVersion(versionInfo, downloadGameVersion(versionInfo.getParentID()));
        }

        return versionInfo;
    }

    private Assets downloadAssetsInfomation(GameVersion version) throws IOException
    {
        Assets info;
        File assetsFile = new File(basepath, "assets/indexes/" + version.getAssets() + ".json");
        AssetInformation assetInfo = version.getAssetInformation();
        URL url;
        FileVerifier verifier;

        if (assetInfo != null)
        {
            url = assetInfo.getURL();
        }
        else
        {
            url = new URL(assetsIndexesURL, version.getAssets() + ".json");
        }

        if (assetInfo != null && assetInfo.isKnownSizeAndHash())
        {
            verifier = new MessageDigestFileVerifier(MessageDigestFileVerifier.SHA_1, assetInfo.getSHA1Hash());
        }
        else
        {
            verifier = new MessageDigestFileVerifier(MessageDigestFileVerifier.MD5, URLUtil.getETag(url).replace("\"", ""));
        }

        return (Assets) downloadJson(assetsFile, url, verifier, Assets.class);
    }

    @Override
    public synchronized void download() throws IOException
    {
        if (running)
        {
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

        for (GameLibrary library : versionInfo.getLibraries())
        {
            if (!library.isAllowed(os, osVersion))
            {
                continue;
            }

            generator = new LibraryDownloaderGenerator(library, librariesDir, os, osArch, repository);
            downloaders.addAll(generator.generateDownloaders());
        }

        generator = new AssetsDownloaderGenerator(downloadAssetsInfomation(versionInfo), assetsObjectsDir, assetsURL);
        downloaders.addAll(generator.generateDownloaders());

        if (additionalDownloader != null)
        {
            downloaders.add(additionalDownloader);
        }

        Downloader downloader = new MultipleDownloader(downloaders);
        downloader.registerCallback(callback);
        downloader.download();

        running = false;
    }

    @Override
    public void stop()
    {
        running = false;
    }

    @Override
    public boolean isRunning()
    {
        return running;
    }

    @Override
    public File getCurrentFile()
    {
        return currentFile;
    }

    @Override
    public int getOverallPercentage()
    {
        return overallPercentage;
    }

    @Override
    public long getCurrentFileSize()
    {
        return fileSize;
    }

    @Override
    public long getCurrentDownloadedSize()
    {
        return fileDownloaded;
    }

    @Override
    public void registerCallback(DownloaderCallback callback)
    {
        callbackList.add(callback);
    }

    @Override
    public void removeCallback(DownloaderCallback callback)
    {
        callbackList.remove(callback);
    }
}
