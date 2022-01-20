// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class LibraryDownloadInfomation
{

    @SerializedName("artifact")
    private PathDownloadInformation artifactInfo;
    @SerializedName("classifiers")
    private Map<String, PathDownloadInformation> classfiersInfo;

    public LibraryDownloadInfomation()
    {

    }

    public LibraryDownloadInfomation(PathDownloadInformation artifact, Map<String, PathDownloadInformation> classfiers)
    {
        artifactInfo = artifact;
        classfiersInfo = classfiers;
    }

    public PathDownloadInformation getArtifactInfo()
    {
        return artifactInfo;
    }

    public Map<String, PathDownloadInformation> getClassfiersInfo()
    {
        return classfiersInfo;
    }
}
