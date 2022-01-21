// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.minecraft.authentication.yggdrasil;

import com.google.gson.annotations.SerializedName;

public class Property {

    @SerializedName("name")
    private String name;
    @SerializedName("value")
    private String value;

    public Property() {

    }

    public Property(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
