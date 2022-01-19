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

package com.lion328.xenonlauncher.minecraft.launcher.json.data;

import com.google.gson.annotations.SerializedName;
import com.lion328.xenonlauncher.util.OS;

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

    public String getNative(OS os, OS.Architecture arch)
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
        return getNative(OS.getCurrentOS(), OS.getCurrentArchitecture());
    }
}
