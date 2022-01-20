// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.annotations.SerializedName;

public enum ReleaseType {

    @SerializedName("old_alpha") OLD_ALPHA("old_alpha"),
    @SerializedName("old_beta") OLD_BETA("old_beta"),
    @SerializedName("snapshot") SNAPSHOT("snapshot"),
    @SerializedName("release") RELEASE("release");

    private transient String s;

    ReleaseType(String s) {
        this.s = s;
    }

    public String toString() {
        return s;
    }
}