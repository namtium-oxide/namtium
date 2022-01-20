// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.annotations.SerializedName;
import com.lion328.xenonlauncher.downloader.repository.DependencyName;
import com.lion328.xenonlauncher.minecraft.StartupConfiguration;

import java.util.List;

public class GameLibrary
{

    @SerializedName("name")
    private DependencyName name;
    @SerializedName("rules")
    private List<Rule> rules;
    @SerializedName("natives")
    private LibraryNatives natives;
    @SerializedName("extract")
    private ExtractConfiguration extractConfiguration;
    @SerializedName("downloads")
    private LibraryDownloadInfomation downloadInfo;

    public GameLibrary()
    {

    }

    public GameLibrary(DependencyName name, List<Rule> rules, LibraryNatives natives, ExtractConfiguration extractConfiguration, LibraryDownloadInfomation downloadInfo)
    {
        this.name = name;
        this.rules = rules;
        this.natives = natives;
        this.extractConfiguration = extractConfiguration;
        this.downloadInfo = downloadInfo;
    }

    public DependencyName getDependencyName()
    {
        return name;
    }

    public List<Rule> getRules()
    {
        return rules;
    }

    public LibraryNatives getNatives()
    {
        return natives;
    }

    public ExtractConfiguration getExtractConfiguration()
    {
        return extractConfiguration;
    }

    public LibraryDownloadInfomation getDownloadInfo()
    {
        return downloadInfo;
    }

    public boolean isNativesLibrary()
    {
        return natives != null;
    }

    public boolean isJavaLibrary()
    {
        return !isNativesLibrary();
    }

    public boolean isAllowed(StartupConfiguration config)
    {
        return Rule.isAllowed(rules, config);
    }
}
