// Copyright (C) 2016-2022 Waritnan Sookbuntherng
// SPDX-License-Identifier: Apache-2.0

package com.lion328.namtium.downloader.verifier;

import com.lion328.namtium.util.ByteUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MessageDigestFileVerifier implements FileVerifier {

    public static final String MD2 = "MD2";
    public static final String MD5 = "MD5";
    public static final String SHA_1 = "SHA-1";
    public static final String SHA_256 = "SHA-256";
    public static final String SHA_384 = "SHA-384";
    public static final String SHA_512 = "SHA-512";

    private final String algorithm;
    private final byte[] hash;

    public MessageDigestFileVerifier(String algorithm, String hash) {
        this(algorithm, ByteUtil.fromHexString(hash));
    }

    public MessageDigestFileVerifier(String algorithm, byte[] hash) {
        this.algorithm = algorithm;
        this.hash = hash.clone();
    }

    public byte[] getFileHash(File file) throws IOException {
        if (!file.exists()) {
            return null;
        }

        MessageDigest md;

        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        InputStream in = new DigestInputStream(new FileInputStream(file), md);

        byte[] buffer = new byte[8192];

        while (true) {
            if (in.read(buffer) == -1) {
                break;
            }
        }

        in.close();

        return md.digest();
    }

    @Override
    public boolean isValid(File file) throws IOException {
        return Arrays.equals(getFileHash(file), hash);
    }
}
