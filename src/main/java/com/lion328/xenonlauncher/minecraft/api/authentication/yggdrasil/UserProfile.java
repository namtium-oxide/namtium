// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.api.authentication.yggdrasil;

import com.google.gson.annotations.SerializedName;

public class UserProfile
{

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("legacy")
    private boolean legacy = false;

    public UserProfile()
    {

    }

    public UserProfile(String id, String name, boolean legacy)
    {
        this.id = id;
        this.name = name;
        this.legacy = legacy;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public boolean isLegacy()
    {
        return legacy;
    }
}
