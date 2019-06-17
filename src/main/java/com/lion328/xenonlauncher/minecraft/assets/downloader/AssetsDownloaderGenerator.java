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

package com.lion328.xenonlauncher.minecraft.assets.downloader;

import com.lion328.xenonlauncher.downloader.Downloader;
import com.lion328.xenonlauncher.downloader.DownloaderGenerator;
import com.lion328.xenonlauncher.downloader.FileDownloader;
import com.lion328.xenonlauncher.downloader.URLFileDownloader;
import com.lion328.xenonlauncher.downloader.VerifiyFileDownloader;
import com.lion328.xenonlauncher.downloader.verifier.FileVerifier;
import com.lion328.xenonlauncher.downloader.verifier.MessageDigestFileVerifier;
import com.lion328.xenonlauncher.downloader.verifier.MultipleFileVerifier;
import com.lion328.xenonlauncher.downloader.verifier.SizeFileVerifier;
import com.lion328.xenonlauncher.minecraft.assets.data.Assets;
import com.lion328.xenonlauncher.minecraft.assets.data.AssetsObject;
import com.lion328.xenonlauncher.util.URLUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssetsDownloaderGenerator implements DownloaderGenerator
{

    public static final URL DEFAULT_ASSETS_URL = URLUtil.constantURL("http://resources.download.minecraft.net/");
    public static final URL DEFAULT_ASSETS_INDEXES_URL = URLUtil.constantURL("https://s3.amazonaws.com/Minecraft.Download/indexes/");

    private final Assets info;
    private final File assetsDir;
    private final URL downloadURL;

    public AssetsDownloaderGenerator(Assets info, File assetsDir, URL downloadURL)
    {
        this.info = info;
        this.assetsDir = assetsDir;
        this.downloadURL = downloadURL;
    }

    @Override
    public List<Downloader> generateDownloaders() throws IOException
    {
        Map<String, AssetsObject> objects = info.getObjects();
        List<Downloader> downloaders = new ArrayList<>();

        for (Map.Entry<String, AssetsObject> entry : objects.entrySet())
        {
            AssetsObject object = entry.getValue();

            String path = object.getHash().substring(0, 2) + "/" + object.getHash();
            File file = new File(assetsDir, path);
            URL url = new URL(downloadURL, path);

            FileVerifier verifier = new MessageDigestFileVerifier(MessageDigestFileVerifier.SHA_1, object.getHash());
            verifier = new MultipleFileVerifier(new SizeFileVerifier(object.getSize()), verifier);
            FileDownloader downloader = new URLFileDownloader(url, file);
            downloader = new VerifiyFileDownloader(downloader, verifier);

            downloaders.add(downloader);
        }

        return downloaders;
    }
}
