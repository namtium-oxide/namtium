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

import com.lion328.xenonlauncher.minecraft.StartupConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MergedGameVersion extends GameVersion
{

    private GameVersion child, parent;

    public MergedGameVersion(GameVersion child, GameVersion parent)
    {
        this.child = child;
        this.parent = parent;
    }

    public GameVersion getChild()
    {
        return child;
    }

    public GameVersion getParent()
    {
        return parent;
    }

    @Override
    public String getID()
    {
        return child.getID();
    }

    @Override
    public Date getTime()
    {
        return child.getTime();
    }

    @Override
    public Date getReleaseTime()
    {
        return child.getReleaseTime();
    }

    @Override
    public ReleaseType getReleaseType()
    {
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
    public List<GameLibrary> getLibraries()
    {
        List<GameLibrary> libraries = new ArrayList<>(child.getLibraries());
        libraries.addAll(parent.getLibraries());
        return libraries;
    }

    @Override
    public String getMainClass()
    {
        if (child.getMainClass() == null)
        {
            return parent.getMainClass();
        }
        return child.getMainClass();
    }

    @Override
    public int getMinimumLauncherVersion()
    {
        return child.getMinimumLauncherVersion();
    }

    @Override
    public String getAssets()
    {
        if (child.getAssets() == null)
        {
            return parent.getAssets();
        }
        return child.getAssets();
    }

    @Override
    public Map<String, DownloadInformation> getDownloadsInformation()
    {
        if (child.getDownloadsInformation() == null)
        {
            return parent.getDownloadsInformation();
        }
        return child.getDownloadsInformation();
    }

    @Override
    public AssetInformation getAssetInformation()
    {
        if (child.getAssetInformation() == null)
        {
            return parent.getAssetInformation();
        }
        return child.getAssetInformation();
    }

    @Override
    public String getParentID()
    {
        return parent.getID();
    }

    @Override
    public String getJarName()
    {
        if (child.getJarName() == null)
        {
            return parent.getJarName();
        }

        return child.getJarName();
    }
}
