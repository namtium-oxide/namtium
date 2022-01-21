// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.minecraft.manifest;

import com.google.gson.annotations.SerializedName;

import java.net.MalformedURLException;
import java.net.URL;

public class DownloadInformation {

    @SerializedName("url")
    private String url;
    @SerializedName("sha1")
    private String sha1Hash;
    @SerializedName("size")
    private int sizeInBytes;

    public DownloadInformation() {

    }

    public DownloadInformation(URL url, String sha1Hash, int sizeInBytes) {
        this.url = url.toString(); // TODO: restruct the code to store URL (or URI?)
        this.sha1Hash = sha1Hash;
        this.sizeInBytes = sizeInBytes;
    }

    public URL getURL() throws MalformedURLException {
        if (url == null || url.isEmpty()) {
            return null;
        }
        return new URL(url);
    }

    public String getSHA1Hash() {
        return sha1Hash;
    }

    public int getSizeInBytes() {
        return sizeInBytes;
    }
}
