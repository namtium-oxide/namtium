// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.downloader.repository;

import com.lion328.xenonlauncher.downloader.FileDownloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface Repository
{

    FileDownloader getDownloader(DependencyName name, String classifier, String extension, File targetFile) throws IOException;

    InputStream getInputStream(DependencyName name, String classifier, String extension) throws IOException;
}
