// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.annotations.SerializedName;
import com.lion328.xenonlauncher.util.OperatingSystem;

public class LibraryNatives
{

    @SerializedName("linux")
    private String linux;
    @SerializedName("osx")
    private String osx;
    @SerializedName("windows")
    private String windows;

    public LibraryNatives(String linux, String osx, String windows)
    {
        this.linux = linux;
        this.osx = osx;
        this.windows = windows;
    }

    public String getNative(OperatingSystem os, OperatingSystem.Architecture arch)
    {
        String s;
        switch (os)
        {
            default:
            case LINUX:
                s = linux;
                break;
            case WINDOWS:
                s = windows;
                break;
            case OSX:
                s = osx;
                break;
        }
        return s.replace("${arch}", arch.toString());
    }

    public String getNative()
    {
        return getNative(OperatingSystem.getCurrentOS(), OperatingSystem.getCurrentArchitecture());
    }
}
