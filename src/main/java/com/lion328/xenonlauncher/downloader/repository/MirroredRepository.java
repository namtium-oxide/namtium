// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.downloader.repository;

import com.lion328.xenonlauncher.downloader.FileDownloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class MirroredRepository implements Repository
{

    private final Repository main;
    private final Repository mirror;

    public MirroredRepository(Repository main, Repository mirror)
    {
        this.main = main;
        this.mirror = mirror;
    }

    public static Repository fromRepositoryList(List<Repository> repositoryList)
    {
        if (repositoryList.size() < 1)
        {
            return null;
        }

        Iterator<Repository> iterator = repositoryList.iterator();
        Repository repository = iterator.next();

        while (iterator.hasNext())
        {
            repository = new MirroredRepository(repository, iterator.next());
        }

        return repository;
    }

    public Repository getMainRepository()
    {
        return main;
    }

    public Repository getMirrorRepository()
    {
        return mirror;
    }

    @Override
    public FileDownloader getDownloader(DependencyName name, String classifier, String extension, File targetFile) throws IOException
    {
        try
        {
            return main.getDownloader(name, classifier, extension, targetFile);
        }
        catch (IOException e)
        {
            return mirror.getDownloader(name, classifier, extension, targetFile);
        }
    }

    @Override
    public InputStream getInputStream(DependencyName name, String classifier, String extension) throws IOException
    {
        try
        {
            return main.getInputStream(name, classifier, extension);
        }
        catch (IOException e)
        {
            return mirror.getInputStream(name, classifier, extension);
        }
    }
}
