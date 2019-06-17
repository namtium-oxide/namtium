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

package com.lion328.xenonlauncher.minecraft.downloader.generator;

import com.lion328.xenonlauncher.downloader.Downloader;
import com.lion328.xenonlauncher.downloader.DownloaderGenerator;
import com.lion328.xenonlauncher.downloader.FileDownloader;
import com.lion328.xenonlauncher.downloader.URLFileDownloader;
import com.lion328.xenonlauncher.downloader.VerifiyFileDownloader;
import com.lion328.xenonlauncher.downloader.verifier.MessageDigestFileVerifier;
import com.lion328.xenonlauncher.minecraft.downloader.verifier.MinecraftFileVerifier;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.DownloadInformation;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.GameVersion;
import com.lion328.xenonlauncher.util.URLUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class VersionJarDownloaderGenerator implements DownloaderGenerator
{

    public static final URL DEFAULT_MINECRAFT_VERSIONS = URLUtil.constantURL(
            "https://s3.amazonaws.com/Minecraft.Download/versions/");

    private final GameVersion info;
    private final File file;
    private final URL downloadURL;

    public VersionJarDownloaderGenerator(GameVersion info, File file) throws MalformedURLException
    {
        this(info, file, DEFAULT_MINECRAFT_VERSIONS);
    }

    public VersionJarDownloaderGenerator(GameVersion info, File file, URL downloadURL) throws MalformedURLException
    {
        this.info = info;
        this.file = file;
        this.downloadURL = downloadURL;
    }

    public GameVersion getGameVersion()
    {
        return info;
    }

    public File getFile()
    {
        return file;
    }

    @Override
    public List<Downloader> generateDownloaders() throws IOException
    {
        URL jarURL = null;
        FileDownloader downloader;

        if (info.getDownloadsInformation() != null)
        {
            DownloadInformation downloadInfo = info.getDownloadsInformation().get(GameVersion.DOWNLOAD_CLIENT);
            jarURL = downloadInfo.getURL();
            downloader = new URLFileDownloader(jarURL, file);
            downloader = new VerifiyFileDownloader(downloader, new MinecraftFileVerifier(downloadInfo));
        }
        else
        {
            jarURL = new URL(downloadURL, info.getJarName() + "/" + info.getJarName() + ".jar");

            String md5 = URLUtil.getETag(jarURL).trim().replace("\"", "");

            downloader = new URLFileDownloader(jarURL, file);
            downloader = new VerifiyFileDownloader(downloader,
                    new MessageDigestFileVerifier(MessageDigestFileVerifier.MD5, md5));
        }

        return Collections.singletonList((Downloader) downloader);
    }
}
