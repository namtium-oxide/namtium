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

package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.annotations.SerializedName;
import com.lion328.xenonlauncher.minecraft.StartupConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GameVersion
{

    public static final int MINIMUM_LAUNCHER_VERSION_SUPPORTED = 21;
    public static final String DOWNLOAD_CLIENT = "client";
    public static final String DOWNLOAD_SERVER = "server";
    public static final String DOWNLOAD_SERVER_WINDOWS = "windows_server";

    @SerializedName("id")
    private String id;
    @SerializedName("time")
    private Date time;
    @SerializedName("releaseTime")
    private Date releaseTime;
    @SerializedName("type")
    private ReleaseType releaseType;
    @SerializedName("minecraftArguments")
    private String minecraftArguments;
    @SerializedName("libraries")
    private List<GameLibrary> libraries;
    @SerializedName("mainClass")
    private String mainClass;
    @SerializedName("minimumLauncherVersion")
    private int version;
    @SerializedName("assets")
    private String assets = "legacy";
    @SerializedName("downloads")
    private Map<String, DownloadInformation> downloads;
    @SerializedName("assetInfo")
    private AssetInformation assetInformation;
    @SerializedName("inheritsFrom")
    private String parentId;
    @SerializedName("jar")
    private String jar;
    @SerializedName("arguments")
    private Arguments arguments;

    public GameVersion()
    {

    }

    public GameVersion(String id, Date time, Date releaseTime, ReleaseType releaseType, String minecraftArguments, List<GameLibrary> libraries, String mainClass, int version, String assets, Map<String, DownloadInformation> downloads, AssetInformation assetInformation, String parentId, String jar)
    {
        this.id = id;
        this.time = time;
        this.releaseTime = releaseTime;
        this.releaseType = releaseType;
        this.minecraftArguments = minecraftArguments;
        this.libraries = libraries;
        this.mainClass = mainClass;
        this.version = version;
        this.assets = assets;
        this.downloads = downloads;
        this.assetInformation = assetInformation;
        this.parentId = parentId;
        this.jar = jar;
    }

    public String getID()
    {
        return id;
    }

    public Date getTime()
    {
        return time;
    }

    public Date getReleaseTime()
    {
        return releaseTime;
    }

    public ReleaseType getReleaseType()
    {
        return releaseType;
    }

    public List<String> getGameArgumentsOnConfig(StartupConfiguration config)
    {
        if (arguments == null || arguments.getJVMArguments() == null)
        {
            if (minecraftArguments == null)
            {
                return null;
            }

            return Arrays.asList(minecraftArguments.split("\\s+"));
        }

        return arguments.getGameArgumentsOnConfig(config);
    }

    public List<String> getJVMArgumentsOnConfig(StartupConfiguration config)
    {
        return arguments == null ? null : arguments.getJVMArgumentsOnConfig(config);
    }

    public List<GameLibrary> getLibraries()
    {
        return libraries;
    }

    public String getMainClass()
    {
        return mainClass;
    }

    public int getMinimumLauncherVersion()
    {
        return version;
    }

    public String getAssets()
    {
        return assets;
    }

    public Map<String, DownloadInformation> getDownloadsInformation()
    {
        return downloads;
    }

    public AssetInformation getAssetInformation()
    {
        return assetInformation;
    }

    public String getParentID()
    {
        return parentId;
    }

    public String getJarName()
    {
        if (jar == null)
        {
            return id;
        }

        return jar;
    }

    public String getJarPath()
    {
        return "versions/" + getJarName() + "/" + getJarName() + ".jar";
    }

    public File getJarFile(File basepath)
    {
        return new File(basepath, getJarPath());
    }
}
