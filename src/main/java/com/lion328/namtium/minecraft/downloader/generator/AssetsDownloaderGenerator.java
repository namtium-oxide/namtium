// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.minecraft.downloader.generator;

import com.lion328.namtium.downloader.Downloader;
import com.lion328.namtium.downloader.DownloaderGenerator;
import com.lion328.namtium.downloader.FileDownloader;
import com.lion328.namtium.downloader.URLFileDownloader;
import com.lion328.namtium.downloader.VerifiyFileDownloader;
import com.lion328.namtium.downloader.verifier.FileVerifier;
import com.lion328.namtium.downloader.verifier.MessageDigestFileVerifier;
import com.lion328.namtium.downloader.verifier.MultipleFileVerifier;
import com.lion328.namtium.downloader.verifier.SizeFileVerifier;
import com.lion328.namtium.minecraft.assets.Assets;
import com.lion328.namtium.minecraft.assets.AssetsObject;
import com.lion328.namtium.util.URLUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssetsDownloaderGenerator implements DownloaderGenerator {

    public static final URL DEFAULT_ASSETS_URL = URLUtil.constantURL("http://resources.download.minecraft.net/");
    public static final URL DEFAULT_ASSETS_INDEXES_URL = URLUtil.constantURL("https://s3.amazonaws.com/Minecraft.Download/indexes/");

    private final Assets info;
    private final File assetsDir;
    private final URL downloadURL;

    public AssetsDownloaderGenerator(Assets info, File assetsDir, URL downloadURL) {
        this.info = info;
        this.assetsDir = assetsDir;
        this.downloadURL = downloadURL;
    }

    @Override
    public List<Downloader> generateDownloaders() throws IOException {
        Map<String, AssetsObject> objects = info.getObjects();
        List<Downloader> downloaders = new ArrayList<>();

        for (Map.Entry<String, AssetsObject> entry : objects.entrySet()) {
            AssetsObject object = entry.getValue();

            String path = object.getHash().substring(0, 2) + "/" + object.getHash();
            File file = new File(assetsDir, path);
            URL url = new URL(downloadURL, path);

            FileVerifier verifier = new MessageDigestFileVerifier(MessageDigestFileVerifier.SHA_1, object.getHash());
            verifier = new MultipleFileVerifier(new SizeFileVerifier(object.getSize()), verifier);
            FileDownloader downloader = new URLFileDownloader(url, file);
            downloader = new VerifiyFileDownloader(downloader, verifier);

            downloaders.add(downloader);
        }

        return downloaders;
    }
}
