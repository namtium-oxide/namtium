// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.annotations.SerializedName;

public enum RuleAction
{

    @SerializedName("allow")ALLOW("allow"),
    @SerializedName("disallow")DISALLOW("disallow");

    private transient String s;

    RuleAction(String s)
    {
        this.s = s;
    }

    public String toString()
    {
        return s;
    }
}