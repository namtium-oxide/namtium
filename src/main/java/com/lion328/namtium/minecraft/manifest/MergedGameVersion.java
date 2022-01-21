// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.minecraft.manifest;

import com.lion328.namtium.minecraft.StartupConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MergedGameVersion extends GameVersion {

    private GameVersion child, parent;

    public MergedGameVersion(GameVersion child, GameVersion parent) {
        this.child = child;
        this.parent = parent;
    }

    public GameVersion getChild() {
        return child;
    }

    public GameVersion getParent() {
        return parent;
    }

    @Override
    public String getID() {
        return child.getID();
    }

    @Override
    public Date getTime() {
        return child.getTime();
    }

    @Override
    public Date getReleaseTime() {
        return child.getReleaseTime();
    }

    @Override
    public ReleaseType getReleaseType() {
        return child.getReleaseType();
    }

    @Override
    public List<String> getGameArgumentsOnConfig(StartupConfiguration config) {
        List<String> childArgs = child.getGameArgumentsOnConfig(config);
        return childArgs == null ? parent.getGameArgumentsOnConfig(config) : childArgs;
    }

    @Override
    public List<String> getJVMArgumentsOnConfig(StartupConfiguration config) {
        List<String> childArgs = child.getJVMArgumentsOnConfig(config);
        return childArgs == null ? parent.getJVMArgumentsOnConfig(config) : childArgs;
    }

    @Override
    public List<GameLibrary> getLibraries() {
        List<GameLibrary> libraries = new ArrayList<>(child.getLibraries());
        libraries.addAll(parent.getLibraries());
        return libraries;
    }

    @Override
    public String getMainClass() {
        if (child.getMainClass() == null) {
            return parent.getMainClass();
        }
        return child.getMainClass();
    }

    @Override
    public int getMinimumLauncherVersion() {
        return child.getMinimumLauncherVersion();
    }

    @Override
    public String getAssets() {
        if (child.getAssets() == null) {
            return parent.getAssets();
        }
        return child.getAssets();
    }

    @Override
    public Map<String, DownloadInformation> getDownloadsInformation() {
        if (child.getDownloadsInformation() == null) {
            return parent.getDownloadsInformation();
        }
        return child.getDownloadsInformation();
    }

    @Override
    public AssetInformation getAssetInformation() {
        if (child.getAssetInformation() == null) {
            return parent.getAssetInformation();
        }
        return child.getAssetInformation();
    }

    @Override
    public String getParentID() {
        return parent.getID();
    }

    @Override
    public String getJarName() {
        if (child.getJarName() == null) {
            return parent.getJarName();
        }

        return child.getJarName();
    }
}
