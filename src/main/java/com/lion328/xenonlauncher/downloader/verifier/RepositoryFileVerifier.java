// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.downloader.verifier;

import com.lion328.xenonlauncher.downloader.repository.DependencyName;
import com.lion328.xenonlauncher.downloader.repository.Repository;
import com.lion328.xenonlauncher.util.io.StreamUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class RepositoryFileVerifier implements FileVerifier {

    private final MessageDigestFileVerifier verifier;

    public RepositoryFileVerifier(Repository repo, DependencyName name, String classifier, String algorithm) throws IOException {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        InputStream in = repo.getInputStream(name, classifier, algorithm.toLowerCase().replace("-", ""));

        StreamUtil.pipeStream(in, array);

        verifier = new MessageDigestFileVerifier(algorithm, new String(array.toByteArray(), "UTF-8").trim());
    }

    @Override
    public boolean isValid(File file) throws IOException {
        return verifier.isValid(file);
    }
}
