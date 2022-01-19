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
import com.lion328.xenonlauncher.downloader.repository.DependencyName;
import com.lion328.xenonlauncher.downloader.repository.Repository;
import com.lion328.xenonlauncher.downloader.verifier.FileVerifier;
import com.lion328.xenonlauncher.minecraft.downloader.verifier.MinecraftFileVerifier;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.DownloadInformation;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.GameLibrary;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.LibraryDownloadInfomation;
import com.lion328.xenonlauncher.minecraft.launcher.json.data.PathDownloadInformation;
import com.lion328.xenonlauncher.util.OS;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LibraryDownloaderGenerator implements DownloaderGenerator
{

    private final GameLibrary library;
    private final File librariesDir;
    private final OS os;
    private final OS.Architecture arch;
    private final Repository defaultRepository;

    public LibraryDownloaderGenerator(GameLibrary library, File librariesDir, OS os, OS.Architecture arch, Repository defaultRepository)
    {
        this.library = library;
        this.librariesDir = librariesDir;
        this.os = os;
        this.arch = arch;
        this.defaultRepository = defaultRepository;
    }

    private FileDownloader getDownloader(File file, String classifier) throws IOException
    {
        FileDownloader downloader;
        FileVerifier verifier;

        if (library.getDownloadInfo() != null)
        {
            DownloadInformation downloadInfo;

            if (classifier == null)
            {
                downloadInfo = library.getDownloadInfo().getArtifactInfo();
            }
            else
            {
                downloadInfo = library.getDownloadInfo().getClassfiersInfo().get(classifier);
            }

            downloader = new URLFileDownloader(downloadInfo.getURL(), file);
            verifier = new MinecraftFileVerifier(downloadInfo);
        }
        else
        {
            DependencyName name = library.getDependencyName();
            verifier = new MinecraftFileVerifier(defaultRepository, name, classifier);
            downloader = defaultRepository.getDownloader(name, classifier, null, file);
        }

        return new VerifiyFileDownloader(downloader, verifier);
    }

    @Override
    public List<Downloader> generateDownloaders() throws IOException
    {
        List<Downloader> downloaders = new ArrayList<>();
        File file;
        LibraryDownloadInfomation downloadInfo = library.getDownloadInfo();
        PathDownloadInformation artifact;
        Map<String, PathDownloadInformation> classifiers;

        if (downloadInfo != null)
        {
            artifact = downloadInfo.getArtifactInfo();
            classifiers = downloadInfo.getClassfiersInfo();
        }
        else
        {
            artifact = new PathDownloadInformation();
            classifiers = Collections.emptyMap();
        }

        if (library.isJavaLibrary())
        {
            if (artifact.getPath() == null)
            {
                file = library.getDependencyName().getFile(librariesDir);
            }
            else
            {
                file = new File(librariesDir, library.getDownloadInfo().getArtifactInfo().getPath());
            }

            downloaders.add(getDownloader(file, null));
        }

        if (library.isNativesLibrary())
        {
            String classifier = library.getNatives().getNative(os, arch);

            if (classifiers.get(classifier) == null || classifiers.get(classifier).getPath() == null)
            {
                file = library.getDependencyName().getFile(librariesDir, classifier);
            }
            else
            {
                file = new File(librariesDir, library.getDownloadInfo().getClassfiersInfo().get(classifier).getPath());
            }

            downloaders.add(getDownloader(file, classifier));
        }

        return downloaders;
    }
}
