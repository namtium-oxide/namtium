// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.downloader.repository;

import java.io.File;

public class DependencyName
{

    private final String packageName;
    private final String name;
    private final String version;

    public DependencyName(String packageName, String name, String version)
    {
        this.packageName = packageName;
        this.name = name;
        this.version = version;
    }

    public DependencyName(String shortName)
    {
        String[] list = shortName.split(":");

        if (list.length < 2)
        {
            throw new IllegalArgumentException("Invalid dependency short name");
        }

        packageName = list[0];
        name = list[1];
        version = list[2];
    }

    public String getPath()
    {
        return getPath("");
    }

    public String getPath(String classifier)
    {
        if (classifier == null)
        {
            classifier = "";
        }
        else
        {
            classifier = "-" + classifier;
        }

        return packageName.replace('.', '/') + "/" +
                name + "/" +
                version + "/" +
                name + "-" + version + classifier + ".jar";
    }

    public File getFile(File librariesDir)
    {
        return getFile(librariesDir, null);
    }

    public File getFile(File librariesDir, String classifier)
    {
        return new File(librariesDir, getPath(classifier));
    }

    public String getShortName()
    {
        return packageName + ":" + name + ":" + version;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public String getName()
    {
        return name;
    }

    public String getVersion()
    {
        return version;
    }

    public String toString()
    {
        return getShortName();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        DependencyName that = (DependencyName) o;

        if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null)
        {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null)
        {
            return false;
        }
        return version != null ? version.equals(that.version) : that.version == null;

    }

    @Override
    public int hashCode()
    {
        int result = packageName != null ? packageName.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}
