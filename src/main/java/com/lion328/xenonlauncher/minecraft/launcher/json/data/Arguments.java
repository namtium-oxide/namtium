// Copyright (C) 2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.annotations.SerializedName;
import com.lion328.xenonlauncher.minecraft.StartupConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Arguments {

    @SerializedName("game")
    private List<ArgumentUnit> game;
    @SerializedName("jvm")
    private List<ArgumentUnit> jvm;

    public List<ArgumentUnit> getGameArguments() {
        return game == null ? null : Collections.unmodifiableList(game);
    }

    public List<ArgumentUnit> getJVMArguments() {
        return jvm == null ? null : Collections.unmodifiableList(jvm);
    }

    public List<String> getGameArgumentsOnConfig(StartupConfiguration config) {
        if (game == null) {
            return null;
        }

        List<String> args = new ArrayList<>();
        for (ArgumentUnit unit : game) {
            args.addAll(unit.getValueOnConfig(config));
        }
        return args;
    }

    public List<String> getJVMArgumentsOnConfig(StartupConfiguration config) {
        if (jvm == null) {
            return null;
        }

        List<String> args = new ArrayList<>();
        for (ArgumentUnit unit : jvm) {
            args.addAll(unit.getValueOnConfig(config));
        }
        return args;
    }
}
