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
