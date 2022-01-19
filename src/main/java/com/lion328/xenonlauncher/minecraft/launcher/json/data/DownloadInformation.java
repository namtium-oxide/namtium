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

import java.net.MalformedURLException;
import java.net.URL;

public class DownloadInformation
{

    @SerializedName("url")
    private String url;
    @SerializedName("sha1")
    private String sha1Hash;
    @SerializedName("size")
    private int sizeInBytes;

    public DownloadInformation()
    {

    }

    public DownloadInformation(URL url, String sha1Hash, int sizeInBytes)
    {
        this.url = url.toString(); // TODO: restruct the code to store URL (or URI?)
        this.sha1Hash = sha1Hash;
        this.sizeInBytes = sizeInBytes;
    }

    public URL getURL() throws MalformedURLException {
        if (url == null || url.isEmpty()) {
            return null;
        }
        return new URL(url);
    }

    public String getSHA1Hash()
    {
        return sha1Hash;
    }

    public int getSizeInBytes()
    {
        return sizeInBytes;
    }
}
