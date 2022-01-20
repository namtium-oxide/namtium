/*
 * Copyright (c) 2018 Waritnan Sookbuntherng
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.lion328.xenonlauncher.util;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public enum OperatingSystem
{

    @SerializedName("win")WINDOWS("win"),
    @SerializedName("osx")OSX("osx"),
    @SerializedName("linux")LINUX("linux");

    private static OperatingSystem currentOS = null;
    private static Architecture currentArch = null;
    private static File appdata = null;
    private final String s;

    OperatingSystem(String s)
    {
        this.s = s;
    }

    public static OperatingSystem fromString(String s)
    {
        for (OperatingSystem os : values())
        {
            if (os.toString().equals(s))
            {
                return os;
            }
        }
        return null;
    }

    public static OperatingSystem getCurrentOS()
    {
        if (currentOS == null)
        {
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("win"))
            {
                currentOS = OperatingSystem.WINDOWS;
            }
            else if (osName.contains("mac"))
            {
                currentOS = OperatingSystem.OSX;
            }
            else
            {
                currentOS = OperatingSystem.LINUX;
            }
        }
        return currentOS;
    }

    public static String getCurrentVersion()
    {
        return System.getProperty("os.version");
    }

    public static Architecture getCurrentArchitecture()
    {
        if (currentArch == null)
        {
            currentArch = Architecture.fromString(System.getProperty("sun.arch.data.model"));
        }

        return currentArch;
    }

    public static String getCurrentArchitectureName()
    {
        return getCurrentArchitecture().toString();
    }

    public static File getApplicationDataDirectory()
    {
        if (appdata == null)
        {
            File home = new File(System.getProperty("user.home", "."));

            switch (getCurrentOS())
            {
                default:
                case LINUX:
                    appdata = home;
                    break;
                case WINDOWS:
                    String winAppdata = System.getenv("APPDATA");

                    if (winAppdata == null)
                    {
                        appdata = home;
                    }
                    else
                    {
                        appdata = new File(winAppdata);
                    }

                    break;
                case OSX:
                    appdata = new File(home, "Library/Application Support/");
                    break;
            }
        }
        return appdata;
    }

    public String toString()
    {
        return s;
    }

    public enum Architecture
    {

        ARCH_32("32"), ARCH_64("64"), UNKNOWN("unknown");

        private String s;

        Architecture(String s)
        {
            this.s = s;
        }

        @Override
        public String toString()
        {
            return s;
        }

        public static Architecture fromString(String s)
        {
            for (Architecture arch : values())
            {
                if (arch.s.equals(s))
                {
                    return arch;
                }
            }

            return UNKNOWN;
        }
    }
}
