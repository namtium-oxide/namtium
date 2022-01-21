// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.minecraft.manifest;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

public class PathDownloadInformation extends DownloadInformation {

    @SerializedName("path")
    private String path;

    public PathDownloadInformation() {

    }

    public PathDownloadInformation(URL url, String sha1Hash, int sizeInBytes, String path) {
        super(url, sha1Hash, sizeInBytes);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
