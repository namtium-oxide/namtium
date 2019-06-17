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
    public FileDownloader getDownloader(DependencyName name, String classifier, String extension,
            File targetFile) throws IOException
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
