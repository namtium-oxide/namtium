// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.minecraft.downloader.verifier;

import com.lion328.namtium.downloader.repository.DependencyName;
import com.lion328.namtium.downloader.repository.Repository;
import com.lion328.namtium.downloader.verifier.FileVerifier;
import com.lion328.namtium.downloader.verifier.MessageDigestFileVerifier;
import com.lion328.namtium.downloader.verifier.MultipleFileVerifier;
import com.lion328.namtium.downloader.verifier.RepositoryFileVerifier;
import com.lion328.namtium.downloader.verifier.SizeFileVerifier;
import com.lion328.namtium.minecraft.manifest.DownloadInformation;

import java.io.File;
import java.io.IOException;

public class MinecraftFileVerifier implements FileVerifier {

    private final FileVerifier verifier;

    public MinecraftFileVerifier(Repository repo, DependencyName name, String classifier) throws IOException {
        verifier = new RepositoryFileVerifier(repo, name, classifier, MessageDigestFileVerifier.SHA_1);
    }

    public MinecraftFileVerifier(DownloadInformation info) {
        FileVerifier tmp = new MessageDigestFileVerifier(MessageDigestFileVerifier.SHA_1, info.getSHA1Hash());
        verifier = new MultipleFileVerifier(new SizeFileVerifier(info.getSizeInBytes()), tmp);
    }

    @Override
    public boolean isValid(File file) throws IOException {
        return verifier.isValid(file);
    }
}
