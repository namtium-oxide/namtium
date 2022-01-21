// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.minecraft.authentication.yggdrasil;

import com.google.gson.annotations.SerializedName;

public class GameAgent {

    @SerializedName("name")
    private String name;
    @SerializedName("version")
    private int version;

    public GameAgent() {

    }

    public GameAgent(String name, int version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }
}
