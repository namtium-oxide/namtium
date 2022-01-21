// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.downloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class GZIPFileDownloader extends URLFileDownloader {

    public GZIPFileDownloader(URL url, File file) {
        super(url, file);
    }

    public GZIPFileDownloader(URL url, File file, int bufferSize) {
        super(url, file, bufferSize);
    }

    @Override
    protected InputStream buildInputStream(InputStream parent) throws IOException {
        return new GZIPInputStream(parent);
    }
}
