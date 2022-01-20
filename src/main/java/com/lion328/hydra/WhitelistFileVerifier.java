// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.hydra;

import com.lion328.xenonlauncher.downloader.verifier.FileVerifier;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class WhitelistFileVerifier implements FileVerifier
{

    private final List<String> whitelist;
    private final Path gameDirectoryPath;

    public WhitelistFileVerifier(List<String> whitelist, Path gameDirectoryPath)
    {
        this.whitelist = whitelist;
        this.gameDirectoryPath = gameDirectoryPath;
    }

    @Override
    public boolean isValid(File file)
    {
        if (!file.exists())
        {
            return false;
        }

        file = file.getAbsoluteFile();
        String filePathRelativize = gameDirectoryPath.relativize(file.toPath()).toString();
        String filePathRelativizeURI = filePathRelativize.replace('\\', '/');

        if (file.isDirectory() && !filePathRelativizeURI.endsWith("/"))
        {
            filePathRelativizeURI = filePathRelativizeURI + "/";
        }

        for (String filename : whitelist)
        {
            if (filePathRelativizeURI.equals(filename))
            {
                return true;
            }

            if (filename.endsWith("/") && filePathRelativizeURI.startsWith(filename))
            {
                return true;
            }
        }

        return false;
    }
}
