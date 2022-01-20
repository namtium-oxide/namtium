// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.xenonlauncher.downloader.verifier;

import java.io.File;
import java.io.IOException;

public class SizeFileVerifier implements FileVerifier {

    private final long fileSize;

    public SizeFileVerifier(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getFileSize() {
        return fileSize;
    }

    @Override
    public boolean isValid(File file) throws IOException {
        return file.length() == fileSize;
    }
}
