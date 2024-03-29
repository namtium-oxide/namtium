// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.minecraft.manifest.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lion328.namtium.downloader.repository.DependencyName;
import com.lion328.namtium.minecraft.manifest.ArgumentUnit;

public class GsonFactory {

    private static Gson defaultGson;

    public static Gson create() {
        if (defaultGson == null) {
            defaultGson = new GsonBuilder()
                    .registerTypeAdapter(DependencyName.class, new DependencyNameTypeAdapter())
                    .registerTypeAdapter(ArgumentUnit.class, new ArgumentUnit.Deserializer())
                    .create();
        }

        return defaultGson;
    }
}
