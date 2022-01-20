// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

public class AssetInformation extends DownloadInformation {

    @SerializedName("totalSize")
    private int totalSizeInBytes = -1;
    @SerializedName("id")
    private String id;

    public AssetInformation(int totalSizeInBytes, String id, URL url, String sha1Hash, int sizeInBytes) {
        super(url, sha1Hash, sizeInBytes);
    }

    public int getTotalSizeInBytes() {
        return totalSizeInBytes;
    }

    public String getID() {
        return id;
    }

    public boolean isKnownSizeAndHash() {
        return totalSizeInBytes != -1 && getSHA1Hash() != null;
    }
}
