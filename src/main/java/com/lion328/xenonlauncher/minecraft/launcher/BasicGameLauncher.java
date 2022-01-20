// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.launcher;

public abstract class BasicGameLauncher implements GameLauncher {

    public BasicGameLauncher() {

    }

    @Override
    public void addGameArgument(String key, String value) {
        addGameArgument("--" + key);
        if (value != null) {
            addGameArgument(value);
        }
    }

    @Override
    public void setMaxMemorySize(int mb) {
        addJVMArgument("-Xmx" + mb + "M");
    }
}
