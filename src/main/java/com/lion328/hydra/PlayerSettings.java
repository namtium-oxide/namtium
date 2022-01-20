// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.hydra;

import com.google.gson.annotations.SerializedName;

public class PlayerSettings {

    public static final int VERSION = 1;
    public static final int DEFAULT_MINIMUM_OF_MAXIMUM_MEMORY = 512;
    public static final int DEFAULT_MAXIMUM_MEMORY = 1024;

    @SerializedName("version")
    private int version = VERSION;
    @SerializedName("playerName")
    private String playerName = "";
    @SerializedName("maximumMemory")
    private int maximumMemory = -1;

    private transient int minOfMaxMem = DEFAULT_MINIMUM_OF_MAXIMUM_MEMORY;
    private transient int defaultMaxMem = DEFAULT_MAXIMUM_MEMORY;

    public PlayerSettings() {

    }

    public PlayerSettings(String playerName, int maximumMemory) {
        this(VERSION, playerName, maximumMemory);
    }

    public PlayerSettings(int version, String playerName, int maximumMemory) {
        this.version = version;
        this.playerName = playerName;
        this.maximumMemory = maximumMemory;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getMaximumMemory() {
        if (maximumMemory == -1) {
            return defaultMaxMem;
        }

        return maximumMemory;
    }

    public void setMaximumMemory(int maximumMemory) throws TooLowMaximumMemoryException {
        if (maximumMemory < minOfMaxMem) {
            throw new TooLowMaximumMemoryException(maximumMemory, minOfMaxMem);
        }

        this.maximumMemory = maximumMemory;
    }

    public int getMinimumOfMaximumMemoryInMB() {
        return minOfMaxMem;
    }

    public void setMinimumOfMaximumMemoryInMiB(int mem) {
        minOfMaxMem = mem;
    }

    public int getDefaultMaximumMemoryInMiB() {
        return defaultMaxMem;
    }

    public void setDefaultMaximumMemoryInMiB(int defaultMaxMem) {
        this.defaultMaxMem = defaultMaxMem;
    }
}
