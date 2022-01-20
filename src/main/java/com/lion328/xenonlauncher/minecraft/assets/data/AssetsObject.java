// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.assets.data;

import com.google.gson.annotations.SerializedName;

public class AssetsObject
{

    @SerializedName("hash")
    private String hash;
    @SerializedName("size")
    private long size;

    public AssetsObject()
    {

    }

    public AssetsObject(String hash, long size)
    {
        this.hash = hash;
        this.size = size;
    }

    public String getHash()
    {
        return hash;
    }

    public long getSize()
    {
        return size;
    }
}
