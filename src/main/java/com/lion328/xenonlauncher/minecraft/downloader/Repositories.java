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

package com.lion328.xenonlauncher.minecraft.downloader;

import com.lion328.xenonlauncher.downloader.repository.MavenRepository;
import com.lion328.xenonlauncher.downloader.repository.MirroredRepository;
import com.lion328.xenonlauncher.downloader.repository.Repository;
import com.lion328.xenonlauncher.util.URLUtil;

import java.util.ArrayList;
import java.util.List;

public class Repositories
{

    public static final List<Repository> DEFAULT_REPOSITORIES;

    public static final MavenRepository MINECRAFT_REPOSITORY;
    public static final MavenRepository MAVENCENTRAL_REPOSITORY;

    static
    {
        DEFAULT_REPOSITORIES = new ArrayList<>();
        MINECRAFT_REPOSITORY = new MavenRepository(URLUtil.constantURL("https://libraries.minecraft.net/"));
        MAVENCENTRAL_REPOSITORY = new MavenRepository(URLUtil.constantURL("http://repo1.maven.org/maven2/"));

        DEFAULT_REPOSITORIES.add(MINECRAFT_REPOSITORY);
        //DEFAULT_REPOSITORY.add(MAVENCENTRAL_REPOSITORY);
    }

    public static final Repository getRepository()
    {
        return MirroredRepository.fromRepositoryList(DEFAULT_REPOSITORIES);
    }
}
