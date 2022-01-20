package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.annotations.SerializedName;
import com.lion328.xenonlauncher.util.OperatingSystem;

public class OperatingSystemIdentifier
{

    @SerializedName("name")
    private OperatingSystem os;
    @SerializedName("version")
    private String versionPattern;

    public OperatingSystemIdentifier()
    {

    }

    public OperatingSystemIdentifier(OperatingSystem os)
    {
        this(os, "(.*?)");
    }

    public OperatingSystemIdentifier(OperatingSystem os, String versionPattern)
    {
        this.os = os;
        this.versionPattern = versionPattern;
    }

    public OperatingSystem getOS()
    {
        return os;
    }

    public boolean isMatch(OperatingSystem os, String version)
    {
        return this.os == os && (versionPattern == null || version.matches(versionPattern));
    }
}
