// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExtractConfiguration
{

    @SerializedName("exclude")
    private List<String> exclude;

    public ExtractConfiguration()
    {

    }

    public ExtractConfiguration(List<String> excludeList)
    {
        exclude = excludeList;
    }

    public List<String> getExcludeList()
    {
        return exclude;
    }
}
