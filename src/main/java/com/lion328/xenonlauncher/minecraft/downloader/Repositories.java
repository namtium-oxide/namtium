// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.minecraft.downloader;

import com.lion328.xenonlauncher.downloader.repository.MavenRepository;
import com.lion328.xenonlauncher.downloader.repository.MirroredRepository;
import com.lion328.xenonlauncher.downloader.repository.Repository;
import com.lion328.xenonlauncher.util.URLUtil;

import java.util.ArrayList;
import java.util.List;

public class Repositories {

    public static final List<Repository> DEFAULT_REPOSITORIES;

    public static final MavenRepository MINECRAFT_REPOSITORY;
    public static final MavenRepository MAVENCENTRAL_REPOSITORY;

    static {
        DEFAULT_REPOSITORIES = new ArrayList<>();
        MINECRAFT_REPOSITORY = new MavenRepository(URLUtil.constantURL("https://libraries.minecraft.net/"));
        MAVENCENTRAL_REPOSITORY = new MavenRepository(URLUtil.constantURL("http://repo1.maven.org/maven2/"));

        DEFAULT_REPOSITORIES.add(MINECRAFT_REPOSITORY);
        //DEFAULT_REPOSITORY.add(MAVENCENTRAL_REPOSITORY);
    }

    public static final Repository getRepository() {
        return MirroredRepository.fromRepositoryList(DEFAULT_REPOSITORIES);
    }
}
