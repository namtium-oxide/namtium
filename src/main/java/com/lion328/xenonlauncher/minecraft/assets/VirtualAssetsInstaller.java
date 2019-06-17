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

package com.lion328.xenonlauncher.minecraft.assets;

import com.lion328.xenonlauncher.downloader.verifier.MessageDigestFileVerifier;
import com.lion328.xenonlauncher.minecraft.assets.data.Assets;
import com.lion328.xenonlauncher.minecraft.assets.data.AssetsObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class VirtualAssetsInstaller
{

    private static final DateFormat lastUsedFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US);

    private final Assets assets;
    private final File objectsDir;
    private final File assetsRoot;
    private final Path objectsDirPath;
    private final File lastUsedFile;

    public VirtualAssetsInstaller(Assets assets, File objectsDir, File assetsRoot)
    {
        this.assets = assets;
        this.objectsDir = objectsDir;
        this.assetsRoot = assetsRoot;

        this.objectsDirPath = objectsDir.toPath();
        this.lastUsedFile = new File(assetsRoot, ".lastused");
    }

    public Assets getAssets()
    {
        return assets;
    }

    public File getObjectsDirectory()
    {
        return objectsDir;
    }

    public File getAssetsRootDirectory()
    {
        return assetsRoot;
    }

    public Path getObjectPath(AssetsObject object)
    {
        return objectsDirPath.resolve(object.getHash().substring(0, 2)).resolve(object.getHash());
    }

    public void install() throws IOException
    {
        File targetFile;

        for (Map.Entry<String, AssetsObject> entry : assets.getObjects().entrySet())
        {
            targetFile = new File(assetsRoot, entry.getKey());

            if (new MessageDigestFileVerifier(MessageDigestFileVerifier.SHA_1, entry.getValue().getHash()).isValid(
                    targetFile))
            {
                continue;
            }

            targetFile.getParentFile().mkdirs();

            Files.copy(getObjectPath(entry.getValue()), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        updateLastUsed(System.currentTimeMillis());
    }

    private void updateLastUsed(long millis) throws IOException
    {
        OutputStream out = new FileOutputStream(lastUsedFile);
        out.write(lastUsedFormat.format(new Date(millis)).getBytes(StandardCharsets.UTF_8));
        out.close();
    }
}
