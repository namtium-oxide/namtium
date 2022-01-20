// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.downloader.repository;

import com.lion328.xenonlauncher.downloader.FileDownloader;
import com.lion328.xenonlauncher.downloader.URLFileDownloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MavenRepository implements Repository
{

    private final URL repositoryURL;

    public MavenRepository(URL repositoryURL)
    {
        this.repositoryURL = repositoryURL;
    }

    @Override
    public FileDownloader getDownloader(DependencyName name, String classifier, String extension, File targetFile) throws IOException
    {
        String path = name.getPath(classifier);

        if (extension == null)
        {
            extension = "";
        }
        else
        {
            extension = "." + extension;
        }

        return new URLFileDownloader(new URL(repositoryURL.toString() + path + extension), targetFile);
    }

    @Override
    public InputStream getInputStream(DependencyName name, String classifier, String extension) throws IOException
    {
        return ((URLFileDownloader) getDownloader(name, classifier, extension, null)).getInputUrl().openStream();
    }
}
