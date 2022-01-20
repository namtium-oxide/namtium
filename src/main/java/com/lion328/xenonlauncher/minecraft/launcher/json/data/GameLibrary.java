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
